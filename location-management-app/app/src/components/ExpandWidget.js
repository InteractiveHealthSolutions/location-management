import * as React from 'react';
import { get, includes } from 'lodash';

import {SplitButton} from 'primereact/splitbutton';
import * as utils from './TreeUtils';

export default class ExpandWidget extends React.Component {
  constructor(props){
    super(props);

    this.state = {
      defaultExpandedLevels: this.props.defaultExpandedLevels,
      nodes: this.props.nodes,
      rowId: this.props.rowId,
      allNodeIds: [],
    }

    let allNodeIds = this.getAllNodesId();
    this.state.allNodeIds = allNodeIds;

    this.resetDefaultExpandedLevels();
  }

  setExpandedKeys = (expandedNodeIds) => {
    let expandedKeys = {};
    expandedNodeIds.map((key) => {
      expandedKeys[key] = true;
    });
    this.props.expandedKeysManager(expandedKeys);
  }

  getAllNodesId = () => {
    return utils.fromTreeToList(this.state.nodes, this.state.rowId);
  }

  expandAllNodes = () => {
    this.setExpandedKeys(this.state.allNodeIds);
  }

  collapseAllNodes = () => {
    this.setExpandedKeys([]);
  }


  getDefaultExpandedLevels = (children, currentLevel, maxLevel, expandedKeys) => {
    if (currentLevel >= maxLevel) {
      return;
    }

    children.map((child) => {
      expandedKeys.push(get(child, this.state.rowId));

      if (child.children) {
        this.getDefaultExpandedLevels(child.children, currentLevel + 1, maxLevel, expandedKeys);
      }
    });

    return expandedKeys;
  }

  resetDefaultExpandedLevels = (level) => {
    let maxLevel = level?level:this.state.defaultExpandedLevels;
    let expandedKeys = [];
    this.getDefaultExpandedLevels(this.state.nodes, 0, maxLevel, expandedKeys);

    this.setExpandedKeys(expandedKeys);
  }

  createMenuFor = (actionType) => {
    let typeButtons = [];
    let idx = 1;
    for (var item of this.props.types) {
      if (item.level > 0) {
        let typeButton = {};

        typeButton['index'] = idx;
        typeButton['label'] = (item.displayName?item.displayName:item.name).toLowerCase();
        typeButton['command'] = (e) => {
          if(includes(actionType, 'expand')){
            this.resetDefaultExpandedLevels(e.item.index);
          }
          else {
            this.resetDefaultExpandedLevels(e.item.index - 1);
          }
        }
        typeButtons.push(typeButton);

        idx++;
      }
    }
    return typeButtons;
  }

    render() {


      return (
          <div>
            <div>
                <SplitButton label="expand all" className="expand-all p-button-secondary" onClick={this.expandAllNodes} model={this.createMenuFor('expand')}></SplitButton>
                <SplitButton label="collapse all" className="collapse-all p-button-secondary" onClick={this.collapseAllNodes} model={this.createMenuFor('collapse')} ></SplitButton>
            </div>
          </div>
      );
  }
}
