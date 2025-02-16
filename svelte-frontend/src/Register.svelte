<script>
    import axios from 'axios';
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    const serverUrl = 'https://orca-app-hv32q.ondigitalocean.app';

    let username = '';
    let password = '';
    let errorMessage = '';
    let successMessage = '';

    const register = async () => {
        if (!username || !password) {
            errorMessage = 'Please fill in all fields.';
            return;
        }

        try {
            const response = await axios.post(`${serverUrl}/api/auth/register`, {
                username,
                password
            });

            if (response.status === 200 || response.status === 201) {
                successMessage = 'Registration successful!';
                errorMessage = '';
                dispatch('register-success');
            }
        } catch (error) {
            errorMessage = error.response ? error.response.data : error.message;
            successMessage = '';
        }
    };
</script>

<style>
    .register-container {
        display: flex;
        justify-content: center;
        align-items: center;
        margin: 1vh;
        background-color: #f0f2f5;
        font-family: Arial, sans-serif;
    }

    .register-box {
        background: white;
        padding: 2rem;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        text-align: center;
        width: 100%;
        max-width: 400px;
    }

    h1 {
        margin-bottom: 1.5rem;
        font-size: 2rem;
        color: #333;
    }

    .form-group {
        margin-bottom: 1rem;
        text-align: left;
    }

    label {
        display: block;
        margin-bottom: 0.5rem;
        font-weight: bold;
        color: #555;
    }

    input {
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 1rem;
    box-sizing: border-box;
  }

  .register-button {
    background-color: #28a745;
    color: white;
    border: none;
    padding: 0.75rem 1.5rem;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s ease;
    width: 100%;
    box-sizing: border-box;
  }

    .register-button:hover {
        background-color: #218838;
    }

    .register-button:active {
        background-color: #1e7e34;
    }

    .error-message {
        color: #dc3545;
        margin-top: 1rem;
    }

    .success-message {
        color: #28a745;
        margin-top: 1rem;
    }
</style>

<div class="register-container">
    <div class="register-box">
        <h1>Register</h1>
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" bind:value={username} placeholder="Enter username" />
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" bind:value={password} placeholder="Enter password" />
        </div>
        <button class="register-button" on:click={register}>Register</button>
        {#if errorMessage}
            <p class="error-message">{errorMessage}</p>
        {/if}
        {#if successMessage}
            <p class="success-message">{successMessage}</p>
        {/if}
    </div>
</div>