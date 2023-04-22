<script lang="ts">
    import "../styles/main.sass"
    import "../scripts/main.ts"
    import {setInitialClassState, modeCurrent, getModeOsPrefers, modeOsPrefers} from '@skeletonlabs/skeleton';
    import LightDarkModeUpdater from "./LightDarkModeUpdater.svelte";
    import ThemeUpdater from "./ThemeUpdater.svelte";


</script>

<svelte:head>
    <title>VoiceChatAPP</title>
</svelte:head>
<LightDarkModeUpdater/>
<ThemeUpdater/>
<slot/>
