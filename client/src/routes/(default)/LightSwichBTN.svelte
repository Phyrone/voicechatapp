<script lang="ts">
    import {
        modeOsPrefers,
        modeUserPrefers,
        modeCurrent,
        setModeCurrent,
        setModeUserPrefers
    } from '@skeletonlabs/skeleton'
    import Icon from '@iconify/svelte';
    import type {IconifyIcon} from '@iconify/types';
    //not changed
    import sunnyOutlineLoop from '@iconify/icons-line-md/sunny-outline-loop';
    import moonLoop from '@iconify/icons-line-md/moon-loop';
    //changed (transition)
    import moonToSunnyOutlineLoopTransition from '@iconify/icons-line-md/moon-to-sunny-outline-loop-transition';
    import sunnyOutlineToMoonLoopTransition from '@iconify/icons-line-md/sunny-outline-to-moon-loop-transition';
    import {onMount} from "svelte";


    let changed: "not_init" | boolean = "not_init";

    let dark: boolean;
    //@ts-ignore
    $:dark = !$modeCurrent


    function onInput() {
        setModeUserPrefers(dark)
    }

    let icon: IconifyIcon;
    $:{
        if (changed===true) {
            if (dark) {
                icon = sunnyOutlineToMoonLoopTransition;
            } else {
                icon = moonToSunnyOutlineLoopTransition;
            }
        } else {
            if (dark) {
                icon = moonLoop;
            } else {
                icon = sunnyOutlineLoop;
            }
        }
        if(changed===false) {
            changed = true;
        }
    }

    onMount(() => {
        changed = false;
    })
</script>
<button class:btn-icon={true} class:variant-ghost={true} on:click={onInput}>
    {#key icon}
        <Icon icon={icon}/>
    {/key}
</button>