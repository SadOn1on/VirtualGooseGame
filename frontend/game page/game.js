document.addEventListener('DOMContentLoaded', function () {
    const username = localStorage.getItem('username');
    const password = localStorage.getItem('password');

    if (!username || !password) {
        window.location.href = 'index.html';
        return;
    }

    // Initial fetch
    fetchGooseAndItems(username, password);

    // Set interval to update every minute
    setInterval(() => fetchGooseAndItems(username, password), 60000);
});

function fetchGooseAndItems(username, password) {
    fetch('http://localhost:8080/goose', {
        headers: {
            'Authorization': 'Basic ' + btoa(username + ':' + password)
        }
    })
        .then(response => response.json())
        .then(goose => {
            document.getElementById('gooseHealth').textContent = goose.health;
            document.getElementById('gooseHunger').textContent = goose.hunger;
            document.getElementById('gooseAge').textContent = goose.age;
            document.getElementById('gooseIllness').textContent = goose.isIll ? 'Yes' : 'No';
            document.getElementById('healthBar').style.width = goose.health + '%';
            document.getElementById('hungerBar').style.width = goose.hunger + '%';
            if (goose.isIll) {
                document.getElementById('illnessBar').classList.add('red');
            } else {
                document.getElementById('illnessBar').classList.remove('red');
            }
        })
        .catch(error => console.error('Error:', error));

    fetch('http://localhost:8080/item', {
        headers: {
            'Authorization': 'Basic ' + btoa(username + ':' + password)
        }
    })
        .then(response => response.json())
        .then(items => {
            const itemsContainer = document.getElementById('items');
            itemsContainer.innerHTML = ''; // Clear the items container
            items.forEach(item => {
                const div = document.createElement('div');
                div.className = 'item-card';
                div.dataset.itemId = item.id; // Store item ID for easy access
                div.innerHTML = `
                <b>${item.name}</b>
                <p>Health: ${item.healthValue}</p>
                <p>Nutrition: ${item.nutritionValue}</p>
                <p>Infected: ${item.infected ? 'Yes' : 'No'}</p>
                <button onclick="useItem(${item.id})">Use</button>
                <button onclick="deleteItem(${item.id})">Delete</button>
            `;
                itemsContainer.appendChild(div);
            });
        })
        .catch(error => console.error('Error:', error));
}

function useItem(itemId) {
    const username = localStorage.getItem('username');
    const password = localStorage.getItem('password');

    fetch(`http://localhost:8080/item/use/${itemId}`, {
        method: 'POST',
        headers: {
            'Authorization': 'Basic ' + btoa(username + ':' + password)
        }
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Failed to use item');
        })
        .then(goose => {
            document.getElementById('gooseHealth').textContent = goose.health;
            document.getElementById('gooseHunger').textContent = goose.hunger;
            document.getElementById('gooseAge').textContent = goose.age;
            document.getElementById('gooseIllness').textContent = goose.isIll ? 'Yes' : 'No';
            document.getElementById('healthBar').style.width = goose.health + '%';
            document.getElementById('hungerBar').style.width = goose.hunger + '%';
            if (goose.isIll) {
                document.getElementById('illnessBar').classList.add('red');
            } else {
                document.getElementById('illnessBar').classList.remove('red');
            }

            // Remove the used item from the list
            const itemElement = document.querySelector(`.item-card[data-item-id="${itemId}"]`);
            if (itemElement) {
                itemElement.remove();
            }
        })
        .catch(error => console.error('Error:', error));
}

function deleteItem(itemId) {
    const username = localStorage.getItem('username');
    const password = localStorage.getItem('password');

    fetch(`http://localhost:8080/item/delete/${itemId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Basic ' + btoa(username + ':' + password)
        }
    })
        .then(() => {
            const itemElement = document.querySelector(`.item-card[data-item-id="${itemId}"]`);
            if (itemElement) {
                itemElement.remove();
            }
        })
        .catch(error => console.error('Error:', error));
}
