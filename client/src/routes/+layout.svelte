<script lang="ts">
    import "../styles/main.sass"
    import "../scripts/main.ts"
    import ThemeUpdater from "./ThemeUpdater.svelte";


</script>

<svelte:head>
    <title>VoiceChatAPP</title>
</svelte:head>
<ThemeUpdater/>
<slot/>
