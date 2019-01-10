import * as React from 'react';
import { get } from 'lodash';

import 'primereact/resources/themes/nova-light/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

import './LocationList.css';

import ExpandWidget from './ExpandWidget';
import LocationForm from './LocationForm';

import { TreeTable } from 'primereact/treetable';
import { Column } from "primereact/column";

import { Button } from 'primereact/button';
import * as utils from './TreeUtils';

import {locations} from './locations.json';
import {locationtypes} from './locationtypes.json';

export default class LocationList extends React.PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      nodes: locations,
      types: locationtypes,
      expandedKeys: {},
      defaultExpandedLevels: 2
    };
  }

  expandedKeysManager = (expandedKeys) =>{
    console.log(expandedKeys);

       this.setState({
           expandedKeys: expandedKeys
       });
   }


  geopointColumn(node, column) {
    if(node.data['latitude']){
      return <span>{node.data['latitude'] + ' , ' + node.data['longitude']}</span>;
    }
    return '';
  }

  locationColumn(node, column) {
    let count = node.children && node.children.length > 0 ? ("("+node.children.length+")"): "";
    if(node.data['displayName']){
      return <span>{node.data['displayName'] + ' ' + count}</span>;
    }
    return <span>{node.data['name'] + ' ' + count}</span>;
  }

  locationTypeColumn(node, column) {
    return <span>{get(node, "data.locationType.locationTypeId")}</span>;
  }

  attributesColumn(node, column) {
    return <span>{get(node, "data.locationType.locationTypeId")}</span>;
  }

  render() {

    return (
      <div>
      <LocationForm  visible/>
      <ExpandWidget rowId="data.locationId" expandedKeysManager={this.expandedKeysManager}
          defaultExpandedLevels={this.state.defaultExpandedLevels} nodes={this.state.nodes}
          types={this.state.types}/>

      <Button label="New Root" onClick={this.handleClick} />

      <TreeTable value={this.state.nodes} expandedKeys={this.state.expandedKeys}
onToggle={e => this.setState({expandedKeys: e.value})}
      >
           <Column field="name" body={this.locationColumn} header='Location' expander className="location"></Column>
           <Column field="locationType" body={this.locationTypeColumn} header="Type"></Column>
           <Column field="otherIdentifier" header="Identifier"></Column>
           <Column field="geopoint" body={this.geopointColumn} header="Latitude, Longitude"></Column>
           <Column field="attributes" body={this.attributesColumn} header="TBD"></Column>
      </TreeTable>
      </div>
    );
  }
}
