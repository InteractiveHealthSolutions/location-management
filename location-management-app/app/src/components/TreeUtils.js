import { get } from 'lodash';

export function fromTreeToList(nodes, property) {
    let list = [];

    extractProperty(nodes, property, list);

    return list;
}

function extractProperty(nodes, property, list) {
  nodes.map((node) => {
    list.push(get(node, property));
    if (node.children) {
      extractProperty(node.children, property, list);
    }
  });
}
