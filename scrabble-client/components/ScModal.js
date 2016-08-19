const React = require('react');
import {Modal, ModalHeader, ModalBody, ModalFooter, Button} from 'elemental';

const Actions = require('../flux/actions/Actions');
const ControlStore = require('../flux/stores/ControlStore');

const ScModal = React.createClass({
    componentDidMount(){
        ControlStore.listen(this.onChange);
    }

    , componentWillUnmount() {
        ControlStore.unlisten(this.onChange);
    }

    , onChange() {
        this.forceUpdate();
    }

    , toggleModal() {
        console.log('ScModal.toggleModal ' + this.props.modalShown);
        Actions.toggleModal("button");
    }

    , render() {
        return (
            <Modal isOpen={this.props.modalShown} onCancel={this.toggleModal}>
                <ModalHeader text="Web Scrabble"/>
                <ModalBody>[...]</ModalBody>
                <ModalFooter>
                    <Button type="link-cancel" onClick={this.toggleModal}>Close</Button>
                </ModalFooter>
            </Modal>
        );
    }
});

module.exports = ScModal;