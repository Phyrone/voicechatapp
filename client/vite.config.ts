import {sveltekit} from '@sveltejs/kit/vite';
import {defineConfig} from 'vitest/config';

function getTarget() {
    switch (process.env.TAURI_PLATFORM) {
        // Tauri supports es2021
        case "windows":
            return "chrome105";
        case "macos":
        case "linux":
            return "safari13";

        default: //not tauri or unknown target
            return "es2020";
    }
}

const IS_TAURI = process.env.TAURI_PLATFORM !== undefined
const TAURI_DEBUG = process.env.TAURI_DEBUG === "true"

function getMinify() {
    return TAURI_DEBUG ? false : IS_TAURI ? "esbuild" : (import.meta?.env?.PROD === true) ? "esbuild" : false;
}


export default defineConfig({
    plugins: [sveltekit()],
    // Vite options tailored for Tauri development and only applied in `tauri dev` or `tauri build`
    // prevent vite from obscuring rust errors
    clearScreen: false,
    // tauri expects a fixed port, fail if that port is not available
    server: {
        port: 1420,
        strictPort: true,
    },

    test: {
        include: ['src/**/*.{test,spec}.{js,ts}']
    },
    envPrefix: ["VITE_", "TAURI_"],
    build: {
        target: getTarget(),
        // don't minify for debug builds
        minify: getMinify(),
        // produce sourcemaps for debug builds
        sourcemap: !getMinify(),
        assetsDir: "static",
    },
});
