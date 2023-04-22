<script lang="ts">
    import {
        modeOsPrefers,
        modeUserPrefers,
        setModeCurrent
    } from "@skeletonlabs/skeleton";
    import {onDestroy, onMount} from "svelte";

    let darkMode = window.matchMedia('(prefers-color-scheme: dark)')
    let listener = (e) => {
        modeOsPrefers.set(!e.matches)
    }

    onMount(() => {
        darkMode.addEventListener("change", listener)
    })
    onDestroy(() => {
        darkMode.removeEventListener("change", listener)
    })
    $:{
        let currentUserPref = $modeUserPrefers
        if (typeof currentUserPref === "boolean") {
            setModeCurrent(currentUserPref)
        } else if (typeof $modeOsPrefers === "boolean") {
            setModeCurrent($modeOsPrefers)
        } else {
            setModeCurrent(false)
        }
    }


</script>