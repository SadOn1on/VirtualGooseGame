<script>
    import axios from "axios";
    import { createEventDispatcher, onMount } from "svelte";

    const dispatch = createEventDispatcher();

    const serverUrl = "https://orca-app-hv32q.ondigitalocean.app";
    const clientId = "gooseGame-client";
    const authHeaderValue = "Basic " + btoa(`${clientId}:test-client`);
    const redirectUri = "https://goosegame.tech";

    const ACCESS_TOKEN_KEY = "access_token";

    axios.defaults.baseURL = serverUrl;

    const login = () => {
        const requestParams = new URLSearchParams({
            response_type: "code",
            client_id: clientId,
            redirect_uri: redirectUri,
            scope: "read",
        });
        window.location.href = `${serverUrl}/oauth2/authorize?${requestParams}`;
    };

    const getTokens = async (code) => {
        let payload = new FormData();
        payload.append("grant_type", "authorization_code");
        payload.append("code", code);
        payload.append("redirect_uri", redirectUri);
        payload.append("client_id", clientId);

        try {
            const response = await axios.post("/oauth2/token", payload, {
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    Authorization: authHeaderValue,
                },
            });

            if (response.data.access_token) {
                localStorage.setItem(
                    ACCESS_TOKEN_KEY,
                    response.data.access_token,
                );
                console.log("Access token stored");
                dispatch("login");

                const url = new URL(window.location.href);
                url.searchParams.delete("code");
                window.history.replaceState({}, document.title, url.toString());
            }
        } catch (error) {
            console.error(
                "Error fetching tokens:",
                error.response ? error.response.data : error.message,
            );
            dispatch("logout");
        }
    };

    onMount(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get("code");

        if (code) {
            getTokens(code);
        }
    });
</script>

<div class="login-container">
    <div class="login-box">
        <button class="login-button" on:click={login}>Login</button>
    </div>
</div>

<style>
    .login-container {
        display: flex;
        justify-content: center;
        align-items: center;
        margin: 1vh;
        background-color: #f0f2f5;
        font-family: Arial, sans-serif;
    }

    .login-box {
        background: white;
        padding: 2rem;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        text-align: center;
        width: 100%;
        max-width: 400px;
    }

    .login-button {
        background-color: #007bff;
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

    .login-button:hover {
        background-color: #0056b3;
    }

    .login-button:active {
        background-color: #004080;
    }
</style>
