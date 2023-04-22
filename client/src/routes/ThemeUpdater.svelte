<script lang="ts">
    import {selected_theme_id} from "../scripts/selected_theme_id";

    $: window.document.body.dataset.theme = $selected_theme_id;
</script>
<svelte:body data-theme={$selected_theme_id}></svelte:body>