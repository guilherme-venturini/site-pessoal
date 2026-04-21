// ============================================================
// script.js — GV Personal Trainer
// Cada bloco tem comentários explicando o que está acontecendo
// ============================================================


// ============================================================
// 1. ESPERA O HTML CARREGAR ANTES DE EXECUTAR QUALQUER COISA
// "DOMContentLoaded" dispara quando o HTML está pronto.
// Sempre envolva seu JS nesse evento para evitar erros de
// "elemento não encontrado".
// ============================================================
document.addEventListener('DOMContentLoaded', function () {

    // Chamamos todas as funções aqui dentro
    iniciarMenuMobile();
    iniciarScrollHeader();
    iniciarServicos();
    iniciarContadores();
    iniciarAnimacoesScroll();

});


// ============================================================
// 2. MENU MOBILE — hamburguer que abre/fecha o menu
// Conceito: togglear classes CSS. O JS não estiliza diretamente —
// ele adiciona/remove classes, e o CSS cuida do visual.
// ============================================================
function iniciarMenuMobile() {

    // querySelector: pega o primeiro elemento que combinar com o seletor CSS
    const botao = document.querySelector('#menuToggle');
    const menu = document.querySelector('#menuMobile');
    const links = document.querySelectorAll('.menu-link'); // retorna uma lista (NodeList)

    // Se os elementos não existirem no HTML, para aqui para não dar erro
    if (!botao || !menu) return;

    // Adiciona um "ouvinte de evento" no botão
    // Toda vez que clicar, a função dentro é executada
    botao.addEventListener('click', function () {

        // classList.toggle: se a classe existir, remove — se não existir, adiciona
        botao.classList.toggle('ativo');
        menu.classList.toggle('aberto');

    });

    // Fecha o menu ao clicar em qualquer link
    // forEach: percorre cada item da lista e executa a função
    links.forEach(function (link) {
        link.addEventListener('click', function () {
            botao.classList.remove('ativo');
            menu.classList.remove('aberto');
        });
    });

}


// ============================================================
// 3. HEADER COM SOMBRA AO ROLAR
// Conceito: evento "scroll" + window.scrollY
// window.scrollY = quantos pixels o usuário rolou para baixo
// ============================================================
function iniciarScrollHeader() {

    const header = document.querySelector('#header');
    if (!header) return;

    // Esse evento é chamado toda vez que o usuário rola a página
    window.addEventListener('scroll', function () {

        // Se rolou mais de 50px, adiciona a classe 'scrolled'
        // O CSS usa essa classe para mostrar a sombra
        if (window.scrollY > 50) {
            header.classList.add('scrolled');
        } else {
            header.classList.remove('scrolled');
        }

    });

}


// ============================================================
// 4. CARDS DE SERVIÇO — abrir e fechar com animação
// Conceito: manipular classes CSS com classList
// A animação vem do CSS (max-height: 0 → max-height: 300px)
// ============================================================
function iniciarServicos() {

    // querySelectorAll retorna TODOS os elementos com essa classe
    const cards = document.querySelectorAll('.servico-card');
    if (cards.length === 0) return;

    // Percorre cada card
    cards.forEach(function (card) {

        card.addEventListener('click', function () {

            // Verifica se esse card já está aberto
            const estaAberto = card.classList.contains('ativo');

            // Fecha TODOS os cards primeiro (comportamento accordion)
            // Isso garante que só um fique aberto por vez
            cards.forEach(function (c) {
                c.classList.remove('ativo');
            });

            // Se o card clicado estava fechado, abre ele agora
            // Se estava aberto, não faz nada — ele já foi fechado acima
            if (!estaAberto) {
                card.classList.add('ativo');
            }

        });

    });

}


// ============================================================
// 5. CONTADORES ANIMADOS (0 → número alvo)
// Conceito: IntersectionObserver + setInterval
//
// IntersectionObserver: observa quando um elemento entra na tela.
// Mais moderno e eficiente do que ficar checando no evento scroll.
//
// setInterval: executa uma função repetidamente em um intervalo
// de tempo, até ser cancelado com clearInterval.
// ============================================================
function iniciarContadores() {

    const contadores = document.querySelectorAll('.contador');
    if (contadores.length === 0) return;

    // Cria um "observador" que vai vigiar os elementos
    const observador = new IntersectionObserver(function (entradas) {

        // "entradas" é a lista de elementos sendo observados
        entradas.forEach(function (entrada) {

            // entrada.isIntersecting = true quando o elemento está visível na tela
            if (entrada.isIntersecting) {

                const elemento = entrada.target;

                // data-alvo é um atributo que colocamos no HTML: data-alvo="50"
                // parseInt converte string para número inteiro
                const alvo = parseInt(elemento.getAttribute('data-alvo'));

                let atual = 0; // começa do zero
                const incremento = Math.ceil(alvo / 60); // calcula quantos somar por vez

                // Executa a função a cada 30 milissegundos
                const intervalo = setInterval(function () {

                    atual += incremento;

                    // Garante que não passe do valor alvo
                    if (atual >= alvo) {
                        atual = alvo;
                        clearInterval(intervalo); // para o intervalo quando chegar ao alvo
                    }

                    elemento.textContent = atual; // atualiza o texto na tela

                }, 30);

                // Para de observar esse elemento (não precisa rodar de novo)
                observador.unobserve(elemento);

            }

        });

    }, { threshold: 0.5 }); // threshold: 0.5 = só dispara quando 50% do elemento estiver visível

    // Manda o observador vigiar cada contador
    contadores.forEach(function (contador) {
        observador.observe(contador);
    });

}


// ============================================================
// 6. ANIMAÇÕES DE ENTRADA AO ROLAR A PÁGINA
// Conceito: IntersectionObserver + classe CSS
//
// Elementos com a classe "revelar" começam invisíveis (no CSS).
// Quando entram na tela, o JS adiciona "visivel" e o CSS anima.
// ============================================================
function iniciarAnimacoesScroll() {

    // Seleciona elementos específicos para ganhar animação
    // Poderia ser qualquer seletor CSS
    const elementos = document.querySelectorAll(
        'section, .servico-card, .stat, .foto-perfil, .btn'
    );

    // Adiciona a classe "revelar" em todos eles via JS
    // Isso é melhor do que colocar no HTML — mantém o HTML limpo
    elementos.forEach(function (el) {
        el.classList.add('revelar');
    });

    // Cria o observador para animar quando entrar na tela
    const observador = new IntersectionObserver(function (entradas) {

        entradas.forEach(function (entrada) {

            if (entrada.isIntersecting) {
                // Adiciona a classe que faz o elemento aparecer (ver CSS)
                entrada.target.classList.add('visivel');

                // Para de observar após animar (eficiência)
                observador.unobserve(entrada.target);
            }

        });

    }, {
        threshold: 0.1,   // dispara quando 10% do elemento estiver visível
        rootMargin: '0px 0px -50px 0px' // "começa" 50px antes de chegar na borda
    });

    elementos.forEach(function (el) {
        observador.observe(el);
    });

}