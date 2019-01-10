import * as React from 'react';
import {Dialog} from 'primereact/dialog';
import {Button} from 'primereact/button';
import 'primereact/resources/themes/nova-light/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

import { Form, FormGroup, FormControl, ControlLabel, HelpBlock } from 'rsuite';
import 'rsuite/dist/styles/rsuite.min.css'

export default class LocationForm extends React.PureComponent {
  constructor(props) {
    super(props);
    this.state = {
      visible: true,
      formValue: {
        name: '',
        email: '',
        password: '',
        textarea: ''
      },
      show: false
    };
  }

  submit = (event) => {
      this.setState({visible: true});
  }

  cancel = (event) => {
      this.setState({visible: false});
  }


  render(){
    const footer = (
        <div>
            <Button label="Yes" icon="pi pi-check" onClick={this.submit} />
            <Button label="No" icon="pi pi-times" onClick={this.cancel} />
        </div>
    );
    return (
      <Dialog header="headerrr" footer={footer}
        modal maximizable closable
        visible={this.state.visible} onHide={(e) => this.setState({visible: false})}>

        <Form
            fluid
            onChange={this.handleChange}
            formValue={this.state.formValue}
          >
          <FormGroup>
            <ControlLabel>Username</ControlLabel>
            <FormControl name="name" />
            <HelpBlock>Required</HelpBlock>
          </FormGroup>
          <FormGroup>
            <ControlLabel>Email</ControlLabel>
            <FormControl name="email" type="email" />
            <HelpBlock>Required</HelpBlock>
          </FormGroup>
          <FormGroup>
            <ControlLabel>Password</ControlLabel>
            <FormControl name="password" type="password" />
          </FormGroup>
          <FormGroup>
            <ControlLabel>Textarea</ControlLabel>
            <FormControl
              rows={5}
              name="textarea"
              componentClass="textarea"
            />
          </FormGroup>
        </Form>

      </Dialog>
    );
  }
}
