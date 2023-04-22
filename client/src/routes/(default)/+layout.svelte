<script lang="ts">
    import {LightSwitch} from '@skeletonlabs/skeleton';
    import {AppBar, AppShell} from "@skeletonlabs/skeleton";
    import LightSwichBTN from "./LightSwichBTN.svelte";
    import ThemeSelector from "./ThemeSelector.svelte";


    function scrollHandler(event: UIEvent & { currentTarget: EventTarget & HTMLDivElement; }) {
        console.log(event.currentTarget.scrollTop);
    }

</script>

<AppShell on:scroll={scrollHandler} regionPage="relative" slotPageHeader="sticky top-0 z-10">
    <svelte:fragment slot="header">
        <AppBar>
            <svelte:fragment slot="lead">
                <button>A</button>
                <button>B</button>
            </svelte:fragment>
            <svelte:fragment slot="trail">
                <LightSwichBTN/>
                <ThemeSelector/>
            </svelte:fragment>

        </AppBar>


    </svelte:fragment>
    <svelte:fragment slot="sidebarLeft">
        <div class="h-full bg-green-200">
            Sidebar Left
        </div>


    </svelte:fragment>
    <svelte:fragment slot="sidebarRight">
        <div class="h-full bg-green-200">
            Sidebar Right
        </div>
    </svelte:fragment>
    <svelte:fragment slot="pageHeader">Page Header</svelte:fragment>
    <!-- Router Slot -->
    <slot/>
    <!-- ---- / ---- -->
    <svelte:fragment slot="pageFooter">
        <div class="bg-blue-200">
            Page Footer
        </div>
    </svelte:fragment>
    <svelte:fragment slot="footer">
        <div class="bg-red-200">
            Footer
        </div>
    </svelte:fragment>
</AppShell>
