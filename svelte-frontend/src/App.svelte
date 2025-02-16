<script>
  import axios from "axios";
  import { onMount } from "svelte";
  import Login from "./Login.svelte";
  import Dashboard from "./Dashboard.svelte";
  import Register from "./Register.svelte";

  const ACCESS_TOKEN_KEY = "access_token";

  let isAuthenticated = false;
  let user = null;

  onMount(() => {
    const token = localStorage.getItem(ACCESS_TOKEN_KEY);
    if (token) {
      isAuthenticated = true;
    }
  });

  const handleLogin = () => {
    isAuthenticated = true;
  };

  const handleLogout = async () => {
    try {
      localStorage.removeItem(ACCESS_TOKEN_KEY);
      const url = new URL(window.location.href);
      url.searchParams.delete("code");
      window.history.replaceState({}, document.title, url.toString());

      const csrfToken = getCsrfToken();
      if (!csrfToken) {
        throw new Error("CSRF token is missing.");
      }

      console.log(csrfToken);
      const response = await axios.post(
        "/logout",
        {},
        {
          withCredentials: true,
          headers: {
          },
        },
      );

      if (response.status === 200 || response.status === 302) {
        window.location.href = "/";
      }

      isAuthenticated = false;
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  const getCsrfToken = () => {
    const csrfMetaTag = document.querySelector('meta[name="_csrf"]');
    if (!csrfMetaTag) {
      console.error("CSRF token meta tag not found!");
      return null;
    }
    return csrfMetaTag.content;
  };
</script>

<main>
  {#if isAuthenticated}
    <Dashboard on:logout={handleLogout} />
  {:else}
    <div class="main-container">
      <Login on:login={handleLogin} />
      <Register />
    </div>
  {/if}
</main>

<style>
  .main-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: 100vh;
    background-color: #f0f2f5;
    font-family: Arial, sans-serif;
  }
</style>
