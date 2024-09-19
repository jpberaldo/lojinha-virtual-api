package dev.servrest.modulos.runner;

import dev.servrest.modulos.tests.CadastroTest;
import dev.servrest.modulos.tests.CarrinhoTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CadastroTest.class,
        CarrinhoTest.class})

public class Runner {
}
