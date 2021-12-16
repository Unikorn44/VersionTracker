
import { BootstrapTreeView } from 'https://unpkg.com/simple-treeview/dist/treeview.bootstrap.js';
let tree = new BootstrapTreeView(document.getElementById('tree'), {

    provider: {
        async getChildren(id) {
            if (!id) {
                return [
                    { id: 'p1', label: 'Données', icon: { classes: ['bi', 'bi-folder'] }, state: 'collapsed' },
                ];
            } else {
                await new Promise((resolve, reject) => setTimeout(resolve, 200));
                switch (id) {
                    case 'p1':
                        return [
                            { id: 'c1', label: 'Lien #1', icon: { classes: ['bi', 'bi-file-earmark'] } },
                            { id: 'c2', label: 'Lien #2', icon: { classes: ['bi', 'bi-file-earmark'] } }
                        ];
                    default:
                        return [];
                }
            }
        }
    }
});
