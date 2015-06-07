function removeProgram(component) {
    var response = confirm('¿Desea eliminar este programa?\n\nRecuerde que al eliminar un programa tiene que tener en cuenta que este no este relacionado con otros datos, por ejemplo que ningun estudiante se haya registrado en este programa');
    if (response) {
        $(component).parent().submit();
    }
}