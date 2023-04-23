<script lang="ts">
    import {theme_settings} from "../scripts/theme_settings";

    $: window.document.body.dataset.theme = $theme_settings;
</script>
<svelte:body data-theme={$theme_settings}></svelte:body>