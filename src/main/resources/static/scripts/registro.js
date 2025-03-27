const telefoneInput = document.getElementById('telefone');
if(telefoneInput != null) {
    telefoneInput.addEventListener('input', () => {
        let telefone = telefoneInput.value.replace(/\D/g, ''); // Remove caracteres não numéricos
        telefone = telefone.replace(/^(\d{2})(\d)/g, '($1) $2'); // Adiciona parênteses e espaço
        telefone = telefone.replace(/(\d{5})(\d)/, '$1-$2'); // Adiciona hífen
        telefoneInput.value = telefone;
    });
}


const inputNomeCompleto = document.getElementById('inputNomeCompleto');
const inputEmail = document.getElementById('inputEmail');
const inputTelefone = document.getElementById('inputTelefone');
const inputEmailContato = document.getElementById('inputEmailContato');
const inputuserRole = document.getElementById('inputUserRole');

const editarBtn = document.getElementById('editarBtn');
const salvarBtn = document.getElementById('salvarBtn');

inputNomeCompleto.disabled = true;
inputEmail.disabled = true;
inputTelefone.disabled = true;
inputEmailContato.disabled = true;
inputuserRole.disabled = true;

editarBtn.addEventListener('click', () => {
    inputNomeCompleto.disabled = false;
    inputEmail.disabled = false;
    inputTelefone.disabled = false;
    inputEmailContato.disabled = false;

    editarBtn.style.display = 'none';
    salvarBtn.style.display = 'block';
});

// salvarBtn.addEventListener('click', () => {
//     inputNomeCompleto.disabled = true;
//     inputEmail.disabled = true;
//     inputTelefone.disabled = true;
//     inputEmailContato.disabled = true;
//
//     editarBtn.style.display = 'block';
//     salvarBtn.style.display = 'none';
//
//     // Aqui você pode adicionar lógica para enviar as alterações para o servidor
//     console.log("Nome Completo:", inputNomeCompleto.value);
//     console.log("Email:", inputEmail.value);
//     console.log("Telefone:", inputTelefone.value);
//     console.log("Email Contato:", inputEmailContato.value);
// });