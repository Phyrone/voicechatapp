import type {PageLoad} from './$types';

export const load = (({params}) => {
    return {
        server_uid: params.server_uid,
    };
}) satisfies PageLoad;