import * as React from 'react';

import { Table } from 'rsuite';
import 'rsuite/dist/styles/rsuite.min.css'
const { Cell } = Table;

export default class LocationCell extends React.Component {
    shouldComponentUpdate(nextProps) {
        return (nextProps.ids !== this.props.ids
             || nextProps.data !== this.props.data);
    }

    render() {
      return (
          <Cell {...this.props} >
            <span>{" " + this.props.rowData['name'] + " -> ("+this.props.rowData.children.length+")"}</span>
          </Cell>
      );
  }
}
