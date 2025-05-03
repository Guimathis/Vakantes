
# Funcionamento do ControllerAdvice

## O que é um ControllerAdvice?

O `@ControllerAdvice` é uma anotação do Spring Framework que permite definir componentes que aplicam comportamentos compartilhados a múltiplos controladores. É uma especialização da anotação `@Component`, o que significa que as classes anotadas com `@ControllerAdvice` são detectadas automaticamente durante a varredura de componentes do Spring.

## Principais funcionalidades do ControllerAdvice

Um ControllerAdvice pode ser usado para:

1. **Adicionar atributos globais ao modelo** - Através do método anotado com `@ModelAttribute`
2. **Manipular exceções globalmente** - Usando métodos anotados com `@ExceptionHandler`
3. **Configurar data binding** - Com métodos anotados com `@InitBinder`

## Implementação no projeto Vakantes

No projeto Vakantes, foi implementado o `UsuarioControllerAdvice` que tem como principal função disponibilizar as informações do usuário logado para todos os controladores da aplicação, eliminando a necessidade de recuperar essas informações em cada método de cada controlador.

### Estrutura do UsuarioControllerAdvice

```java
@ControllerAdvice
public class UsuarioControllerAdvice {

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute("currentUser")
    public Usuario getCurrentUser(Model model, HttpServletRequest request) {
        try {
            String usuarioId = CookieService.getCookie(request, "usuarioId");
            if (usuarioId != null && !usuarioId.isEmpty()) {
                Optional<Usuario> usuario = usuarioService.buscarPorId(Long.valueOf(usuarioId));
                if (usuario.isPresent()) {
                    model.addAttribute("usuarioId", usuario.get().getId());
                    model.addAttribute("usuarioNome", usuario.get().getPrimeiroNome());
                    model.addAttribute("usuarioRole", usuario.get().getUserRole());
                    model.addAttribute("usuarioEmail", usuario.get().getEmail());
                    model.addAttribute("usuarioFoto", usuario.get().getFotoPerfil());

                    return usuario.get();
                }
            }
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error getting user from cookie: " + e.getMessage());
        }

        return null;
    }
}
```

### Como funciona o UsuarioControllerAdvice

1. **Anotação `@ControllerAdvice`**: Indica que esta classe fornece funcionalidades que se aplicam a todos os controladores da aplicação.

2. **Método `getCurrentUser`**:
    - Anotado com `@ModelAttribute("currentUser")`, o que significa que o objeto retornado será adicionado ao modelo com o nome "currentUser"
    - Este método é executado antes de qualquer método de controlador
    - Recupera o ID do usuário do cookie "usuarioId"
    - Busca o usuário no banco de dados usando o UsuarioService
    - Adiciona atributos específicos do usuário ao modelo (ID, nome, papel, email, foto)
    - Retorna o objeto Usuario completo ou null se o usuário não estiver logado

3. **Tratamento de erros**: Captura exceções de codificação não suportada para evitar que erros de cookie interrompam a aplicação.

## Benefícios do ControllerAdvice

A implementação do UsuarioControllerAdvice trouxe vários benefícios para o projeto:

1. **Centralização da lógica de recuperação do usuário**: Toda a lógica para obter o usuário atual está em um único lugar, facilitando a manutenção.

2. **Redução de código duplicado**: Antes, cada controlador precisava recuperar o usuário dos cookies, agora isso é feito automaticamente.

3. **Consistência**: Garante que as informações do usuário sejam recuperadas da mesma forma em toda a aplicação.

4. **Simplificação dos controladores**: Os métodos dos controladores ficaram mais limpos e focados em suas responsabilidades específicas.

5. **Acesso fácil ao usuário atual**: Em qualquer método de controlador, é possível acessar o usuário atual usando `@ModelAttribute("currentUser") Usuario currentUser`.

## Uso nos controladores

Antes da implementação do ControllerAdvice, os controladores precisavam recuperar o usuário manualmente:

```java
@GetMapping("/buscar")
public String listarClientes(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
    String idUsuario = CookieService.getCookie(request, "usuarioId");
    model.addAttribute("idUsuario", idUsuario);
    model.addAttribute("clientes", clienteService.buscarClientePorResponsavel(usuarioService.buscarPorId(Long.valueOf(idUsuario)).get()));
    return "entities/cliente/buscar";
}
```

Depois da implementação, o código ficou mais limpo e direto:

```java
@GetMapping("/buscar")
public String listarClientes(Model model, @ModelAttribute("currentUser") Usuario currentUser) {
    model.addAttribute("clientes", clienteService.buscarClientePorResponsavel(currentUser));
    return "entities/cliente/buscar";
}
```

## Conclusão

O ControllerAdvice é uma poderosa ferramenta do Spring que permite centralizar comportamentos comuns a múltiplos controladores. No projeto Vakantes, o UsuarioControllerAdvice foi implementado para disponibilizar as informações do usuário logado em toda a aplicação, resultando em código mais limpo, manutenível e consistente.

Esta abordagem segue o princípio DRY (Don't Repeat Yourself) e melhora significativamente a arquitetura da aplicação, separando claramente as responsabilidades e reduzindo a duplicação de código.