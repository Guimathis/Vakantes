const telefoneInput = document.getElementById('telefone');
if (telefoneInput != null) {
    telefoneInput.addEventListener('input', () => {
        let telefone = telefoneInput.value.replace(/\D/g, ''); // Remove caracteres não numéricos
        telefone = telefone.replace(/^(\d{2})(\d)/g, '($1) $2'); // Adiciona parênteses e espaço
        telefone = telefone.replace(/(\d{5})(\d)/, '$1-$2'); // Adiciona hífen
        telefoneInput.value = telefone;
    });
}


const labelFoto = document.getElementById('labelFoto');
const inputFoto = document.getElementById('inputFoto');
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
    labelFoto.hidden = false;
    inputFoto.hidden = false;
    inputNomeCompleto.disabled = false;
    inputEmail.disabled = false;
    inputTelefone.disabled = false;
    inputEmailContato.disabled = false;

    editarBtn.style.display = 'none';
    salvarBtn.style.display = 'block';
});

IMask(
    document.getElementById('telefone'),
    {
        mask: '(00) 00000-0000',
        lazy: false
    }
)
IMask(
    document.getElementById('cpf'),
    {
        mask: '000.000.000-00',
        lazy: false
    }
)
IMask(
    document.getElementById('rg'),
    {
        mask: '00.000.000-0',
        lazy: false
    }
)
IMask(
    document.getElementById('cep'),
    {
        mask: '00000-000',
        lazy: false
    }
)
