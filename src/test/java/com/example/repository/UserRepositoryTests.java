/**
 * Segun el enfoque: Una prueba unitaria se divide en tres partes
 *
 * 1. Arrange: Setting up the data that is required for this test case
 * 2. Act: Calling a method or Unit that is being tested.
 * 3. Assert: Verify that the expected result is right or wrong.
 *
 * Segun el enfoque BDD (Behaviour Driven Development). 'Given-When-Then' como lenguaje comun con BDD
* 
* Para definir los casos BDD para una historia de usuario se deben definir bajo el patrón "Given-When-Then"
* , que se define como sigue:
 *
 * 1. given (dado) : Se especifica el escenario, las precondiciones.
 * 2. when (cuando) : Las condiciones de las acciones que se van a ejecutar.
 * 3. then (entonces) : El resultado esperado, las validaciones a realizar.
*
* Un ejemplo practico seria:
*
* Given: Dado que el usuario no ha introducido ningun dato en el formulario.
* When: Cuando se hace click en el boton de enviar.
* Then: Se deben de mostrar los mensajes de validación apropiados.
*
* "Role-Feature-Reason" como lenguaje común con BDD
*
* Este patrón se utiliza en BDD para ayudar a la creación de historias de usuarios. Este se define como:
*
* As a "Como" : Se especifica el tipo de usuario.
* I want "Deseo" : Las necesidades que tiene.
* So that "Para que" : Las caracteristicas para cumplir el objetivo.
*
* Un ejemplo práctico de historia de usuario sería: Como cliente interesado, deseo ponerme en contacto mediante formulario, 
* para que atiendan mis necesidades. 
*
* Parece que BDD y TDD son la misma cosa, pero la principal diferencia entre ambas esta en el alcance. TDD es una practica de desarrollo 
* (se enfoca en como escribir el codigo y como deberia trabajar ese codigo) mientras que BDD es una metodologia de equipo (Se enfoca
* en porque debes escribir ese codigo y como se deberia de comportar ese codigo)
*
* En TDD el desarrollador escribe los tests mientras que en BDD el usuario final (o PO o analista) en conjunto con los testers escriben
* los tests (y los Devs solo generan el codigo necesario para ejecutar dichos tests)
*
* Tambien existe ATDD (Acceptance Test Driven Development), que es mas cercana a BDD ya que no es una practica,
* sino una metodologia de trabajo, pero la diferencia esta nuevamente en el alcance, a diferencia de BDD, ATDD se extiende aun 
* mas en profundizar la búsqueda de que lo que se esta haciendo no solo se hace de forma correcta, sino que tambien 
* es lo correcto a hacer.
*
 */

package com.example.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.security.entities.OurUser;
import com.example.security.entities.Role;
import com.example.security.repository.OurUserRepository;

import lombok.var;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest // Para probar entidades y la interface JpaRepository (ni servicios ni controladores...) Por defecto, intentaria utilizar una bbdd que no tenemos
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private OurUserRepository ourUserRepository;

    private OurUser ourUser0;
    
    @BeforeEach
    void setUp() {
        ourUser0 = ourUser0.builder()
            .email("sushi@gmail.com")
            .password("1234")
            .role(Role.USER)
            .build();
    }

    // Test para agregar un user
    @DisplayName("Test para agregar un usuario")
    @Test
    public void testAddUser() {

        // Given 
        OurUser ourUser = OurUser.builder()
            .email("isabel@gmail.com")
            .password("1234")
            .role(Role.ADMIN)
            .build();


        // When
        OurUser ourUserGuardado = ourUserRepository.save(ourUser);

        // Then
        assertThat(ourUserGuardado).isNull();
        assertThat(ourUserGuardado.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test para recuperar un listado de usuarios")
    public void testFindAllUsers() {

    // Given
        OurUser ourUser1 = OurUser.builder()
            .email("isabel@gmail.com")
            .password("1234")
            .role(Role.ADMIN)
            .build();

        // En el given hay que persistir el ourUser0 y ourUser1
        ourUserRepository.save(ourUser0);
        ourUserRepository.save(ourUser1);

    // When
    var usuarios = ourUserRepository.findAll();

    // Then
    assertThat(usuarios).isNotNull();
    assertThat(usuarios.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test para recuperar un usuario por el id")
    public void testFindById() {

        // Given
        ourUserRepository.save(ourUser0);

        // When
        OurUser foundUser = ourUserRepository.findById(ourUser0.getId()).get();

        // Then
        assertThat(foundUser.getId()).isNotEqualTo(0);
    }

    @Test
    @DisplayName("Test para actualizar un usuario")
    public void testUpdateUser() {

        // Given
        ourUserRepository.save(ourUser0);
    
        // When
        OurUser usuarioGuardado = ourUserRepository.findByEmail(ourUser0.getEmail()).get();

        usuarioGuardado.setEmail("susanitaTieneUnRaton@gmail.com");
        usuarioGuardado.setPassword("123456");
        usuarioGuardado.setRole(Role.ADMIN);

        OurUser updatedUser = ourUserRepository.save(usuarioGuardado);

        // Then
        assertThat(updatedUser.getEmail()).isEqualTo("susanitaTieneUnRaton@gmail.com");
    }

    @DisplayName("Test para eliminar un user")
    @Test
    public void testDeleteUser() {

        // given
        ourUserRepository.save(ourUser0);

        // when
        ourUserRepository.delete(ourUser0);
        Optional<OurUser> optionalUser = ourUserRepository.findByEmail(ourUser0.getEmail());

        // then
        assertThat(optionalUser).isEmpty();
    }
}
