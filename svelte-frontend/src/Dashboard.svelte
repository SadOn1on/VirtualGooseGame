<script>
  import axios from 'axios';
  import { createEventDispatcher, onMount } from 'svelte';

  const dispatch = createEventDispatcher();

  const ACCESS_TOKEN_KEY = 'access_token';

  let items = [];
  let goose = null;

  const fetchItems = async () => {
      try {
          const response = await axios.get('https://seashell-app-epta3.ondigitalocean.app/item', {
              headers: { Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN_KEY)}` }
          });
          items = response.data;
      } catch (error) {
          console.error('Failed to fetch items:', error);
      }
  };

  const fetchGoose = async () => {
      try {
          const response = await axios.get('https://seashell-app-epta3.ondigitalocean.app/goose', {
              headers: { Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN_KEY)}` }
          });
          goose = response.data;
          fetchItems();
      } catch (error) {
          console.error('Failed to fetch goose:', error);
      }
  };

  const useItem = async (itemId) => {
      try {
          const response = await axios.post(`https://seashell-app-epta3.ondigitalocean.app/item/use/${itemId}`, {}, {
              headers: { Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN_KEY)}` }
          });
          goose = response.data;
          fetchItems();
      } catch (error) {
          console.error('Failed to use item:', error);
      }
  };

  const deleteItem = async (itemId) => {
      try {
          await axios.delete(`https://seashell-app-epta3.ondigitalocean.app/item/delete/${itemId}`, {
              headers: { Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN_KEY)}` }
          });
          fetchItems();
      } catch (error) {
          console.error('Failed to delete item:', error);
      }
  };

  const handleLogout = () => {
      dispatch('logout');
  };

  onMount(() => {
      fetchGoose();
      fetchItems();
  });
</script>

<style>
    .dashboard-container {
        max-width: 800px;
        margin: 0 auto;
        padding: 2rem;
        font-family: Arial, sans-serif;
        background-color: #f9f9f9;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    h1 {
        font-size: 2.5rem;
        color: #333;
        margin-bottom: 1.5rem;
        text-align: center;
    }

    h2 {
        font-size: 2rem;
        color: #444;
        margin-top: 2rem;
        margin-bottom: 1rem;
    }

    .logout-button-container {
        display: flex;
        justify-content: center;
        margin-bottom: 2rem;
    }

    .logout-button {
        background-color: #ff4d4d;
        color: white;
        border: none;
        padding: 0.75rem 1.5rem;
        border-radius: 4px;
        font-size: 1rem;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    .logout-button:hover {
        background-color: #cc0000;
    }

    .logout-button:active {
        background-color: #990000;
    }

    .goose-status {
        background-color: white;
        padding: 1.5rem;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        margin-bottom: 2rem;
    }

    .goose-status p {
        margin: 0.5rem 0;
        font-size: 1.1rem;
        color: #555;
    }

    .items-list {
        list-style-type: none;
        padding: 0;
    }

    .items-list li {
        background-color: white;
        padding: 1rem;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        margin-bottom: 1rem;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .items-list li p {
        margin: 0;
        font-size: 1rem;
        color: #555;
    }

    .item-buttons button {
        background-color: #007bff;
        color: white;
        border: none;
        padding: 0.5rem 1rem;
        border-radius: 4px;
        font-size: 0.9rem;
        cursor: pointer;
        transition: background-color 0.3s ease;
        margin-left: 0.5rem;
    }

    .item-buttons button:hover {
        background-color: #0056b3;
    }

    .item-buttons button:active {
        background-color: #004080;
    }

    .item-buttons button.delete-button {
        background-color: #ff4d4d;
    }

    .item-buttons button.delete-button:hover {
        background-color: #cc0000;
    }

    .item-buttons button.delete-button:active {
        background-color: #990000;
    }
</style>

<div class="dashboard-container">
    <h1>Dashboard</h1>
    <div class="logout-button-container">
        <button class="logout-button" on:click={handleLogout}>Logout</button>
    </div>

    <h2>Goose Status</h2>
    {#if goose}
        <div class="goose-status">
            <p>Name: {goose.name}</p>
            <p>Age: {goose.age}</p>
            <p>Health: {goose.health}</p>
            <p>Hunger: {goose.hunger}</p>
            <p>Is Ill: {goose.isIll ? 'Yes' : 'No'}</p>
        </div>
    {/if}

    <h2>Items</h2>
    <ul class="items-list">
        {#each items as item}
            <li>
                <p>
                    {item.name} (Health: {item.healthValue}, Nutrition: {item.nutritionValue}, Infected: {item.infected ? 'Yes' : 'No'})
                </p>
                <div class="item-buttons">
                    <button on:click={() => useItem(item.id)}>Use</button>
                    <button class="delete-button" on:click={() => deleteItem(item.id)}>Delete</button>
                </div>
            </li>
        {/each}
    </ul>
</div>