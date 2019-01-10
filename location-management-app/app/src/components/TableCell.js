import * as React from 'react';

import { Table } from 'rsuite';
import 'rsuite/dist/styles/rsuite.min.css'
const { Cell } = Table;

export default class TableCell extends React.Component {
    shouldComponentUpdate(nextProps) {
        return (nextProps.ids !== this.props.ids
             || nextProps.data !== this.props.data);
    }

    render() {
      return (
          <Cell {...this.props} ></Cell>
      );
  }
}
