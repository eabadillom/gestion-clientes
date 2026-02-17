function habilitarHorarioExtra() {
	PF('hx').enable();
	PF('mx').enable();
	PF('bn').enable();
	PF('h').disable();
	PF('m').disable();
	PF('bx').disable();
	PF('testPanel').show();
}

function deshabilitarHorarioExtra() {
	PF('hx').disable();
	PF('mx').disable();
	PF('bx').enable();
	PF('h').enable();
	PF('m').enable();
	PF('bn').disable();
	PF('testPanel').close();
}
