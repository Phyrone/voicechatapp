import adapter from '@sveltejs/adapter-auto';
import staticAdapter from "@sveltejs/adapter-static";
import cloudflareAdapter from "@sveltejs/adapter-cloudflare";
import {vitePreprocess} from '@sveltejs/kit/vite';


/** @type {boolean} */
const isTauri = process.env.TAURI_PLATFORM !== undefined
const BUILD_TARGET_DEFAULT = "static"
const BUILD_TARGET_TAURI = BUILD_TARGET_DEFAULT

/** @type { "static" | "cloudflare" } */
const buildTarget = isTauri ? BUILD_TARGET_TAURI : (process.env.BUILD_TARGET || BUILD_TARGET_DEFAULT).toLowerCase()

/** @type {import('@sveltejs/kit').Adapter} */
function getAdapter() {
    switch (buildTarget) {
        case "static":
            return staticAdapter({
                pages: "dist",
                assets: "dist",
                precompress: !isTauri,
                fallback: isTauri ? "index.html" : "404.html",
                //fallback: "index.html",
            })
        case "cloudflare":
            return cloudflareAdapter()
        default:
            throw new Error(`Unknown build target: ${buildTarget}`)
    }

}

/** @type {import('@sveltejs/kit').Config} */
const config = {
    // Consult https://kit.svelte.dev/docs/integrations#preprocessors
    // for more information about preprocessors
    preprocess: vitePreprocess(),

    kit: {
        // adapter-auto only supports some environments, see https://kit.svelte.dev/docs/adapter-auto for a list.
        // If your environment is not supported or you settled on a specific environment, switch out the adapter.
        // See https://kit.svelte.dev/docs/adapters for more information about adapters.
        adapter: getAdapter(),
        serviceWorker: {
            register: false
        }
    }
};

export default config;
