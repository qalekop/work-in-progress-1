const React = require('react');
import {Modal, ModalHeader, ModalBody, ModalFooter, Button} from 'elemental';

const Actions = require('../flux/actions/Actions');
const DialogStore = require('../flux/stores/DialogStore');

const ScrabbleDialog = React.createClass({
    componentDidMount(){
        DialogStore.listen(this.onChange);
    }

    , componentWillUnmount() {
        DialogStore.unlisten(this.onChange);
    }

    , onChange() {
        this.forceUpdate();
    }

    , hideModal() {
        Actions.hideDialog();
    }

    , render() {
        if (this.props.showCloseButton) {
            return (
                <Modal isOpen={this.props.modalShown} onCancel={this.hideModal} width="medium">
                    <ModalHeader text="Web Scrabble" showCloseButton={this.props.showCloseButton} onClose={this.hideModal}/>
                    <ModalBody>{this.props.text}</ModalBody>
                    <ModalFooter>
                        <Button type="link-cancel" onClick={this.hideModal}>Close</Button>
                    </ModalFooter>
                </Modal>
            );
        } else {
            return (
                <Modal isOpen={this.props.modalShown} width="medium">
                    <ModalHeader text="Web Scrabble" showCloseButton={this.props.showCloseButton}/>
                    <ModalBody>{this.props.text}</ModalBody>
                </Modal>
            );
        }
    }
});

module.exports = ScrabbleDialog;