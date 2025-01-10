## Getting Started

In this Springboot Duo Exercise we will take this basic Springboot application and transform it into a modern application using the power of GitLab Duo. All of the work will be done in a browser through GitLab and the supported Web IDE. Post completing the guide feel free to run through the same exercise on your own machine or hook into a cloud provider of your choice to see the application deployed out.

## Planning Our Work

1. First things first we want to be able to track our work to record all that duo has helped us with. We will start by navigating to our project in GitLab and then using the left hand navigation menu to click through **Plan > Issues**. On the resulting page click **New issue**
2. We will first use **GitLab Duo** to generate an issue description for our work. Within the description space click the **tanuki icon** then click **generate issue description**.
3. In the resulting prompt add the wording below then click **Submit**:

    ```
    Use GitLab duo to modernize our spring boot application. We will be adding a number of restful controllers to interact with our backend database. Finally we will write some unit tests and secure our application using GitLab Security and Duo. All of these should be required checkboxes on the issue.
    ```
4. Next in the issue _title_ add 'Modernizing out springboot application with GitLab Duo;' and then assign the issue to yourself. Click 'Create Issue'
5. Now that we have created our issue lets also kick off a related merge request. On our issue view click **Create merge request**.
6. On the resulting merge request view first uncheck **mark as draft**, assign the merge request to ourselves, then click **Create merge request**. Now all of our work will be tracked through this merge request enabling shifting left.

## Setting up our pipeline.

1. Next we want to add a CICD pipeline to our project so that we can securely build and scan our code with every commit. Use the left hand navigation menu to click through **build >pipeline editor** then in the top left change the branch to our merge request branch ( which should be main by default ). 

> Please note fully deploying the application is out of scope for this POV.

2. Click 'Configure pipeline' button. In the editor copy and paste the below config to be the basis for our pipeline:

    ```

    services:
      - docker:dind

    include:
      - template: Jobs/Test.gitlab-ci.yml
      - template: Jobs/Code-Quality.gitlab-ci.yml
      - template: Jobs/SAST.gitlab-ci.yml
      - template: Jobs/SAST-IaC.gitlab-ci.yml
      - template: Jobs/Container-Scanning.gitlab-ci.yml
      - template: Jobs/Dependency-Scanning.gitlab-ci.yml
      - template: Jobs/Secret-Detection.gitlab-ci.yml
      - template: Jobs/Build.gitlab-ci.yml

    variables:
      CODE_QUALITY_DISABLED: true
      DOCKER_DRIVER: overlay2
      DOCKER_TLS_CERTDIR: ""

    test:
      image: gradle:8.3.0-jdk17-alpine
      script: ./gradlew test
      allow_failure: true
    ```
2. This pipeline will use templates to build & test our code. To see what code our templates have added, click the **Full configuration tab**
3. We can also see what order our jobs will run by clicking the **Visualize tab**.
Please note that we expect our _test_ job to fail as is because we have not yet added unit tests. That is why we have added the _allow_failure_ option. Later on in the guide we will edit the tests using Code Suggestions to prove our generated code is working. Lastly scroll to the bottom of the page and click **Commit changes**.
3. Click **Build > Pipelines** to see the triggered pipeline and click on the pipeline number to see the jobs running.

## Understanding Our Current State

1. Now that we have our pipeline and merge request all set up lets open up the Web IDE to start defining the backend of our application. We can do this by clicking **Code** in the top right of our merge request view then selecting **Open in Web IDE**. 
2. If we look through the files we can see this is a basic starting Springboot/Gradle application. We will transform this application to be a new web application that can store data based on different animals we see on a hike.
3. Our application already has and H2 database setup. Spend some time looking through the codebase to get familiar with what has already been created, specifically the existing state of our AnimalQueryController and the services/repository attached.
4. Now that we know our applications current state lets go ahead and navigate to the _AnimalQueryController.java_ file. (animal-hike/src/main/java/com/example/springboot/controller/AnimalQueryController.java)
5. Select the entire code, right click to launch the menu and select 'GitLab Duo Chat -> Explain selected code' . You should see GitLab Duo Chat launch and show you what the code is doing.
6. Explore the other files and code using 'GitLab Duo Chat -> Explain selected code' to understand the application better

## Building Our Methods

1. Our plan to to allow users to create, edit, delete, and get animal entries from our _animals_ DB. To do this we first need to edit the _AnimalQueryService.java_. Notice that we have already attached the animal repository.
2. We will start by writing the methods to get animal entries, specifically get all and get by id. After line 15 add the following comment and hit tab to accept the prompted code:

    ```
    // Write a new method that uses the above animalRepository to return all using .findAll
    ```

    > We suggest removing the creation comment after code has been generated

3. Once the code is accepted look it over and ensure all of the spacing is correct. We will provide an answer key to reference at the end of this section, please note Code Suggestions are generative and there are multiple answers. The answer key is only there as a reference. After our new function that gets all we want to add one that searches by specific id. Use the below prompt to do so:

    ```
    // Write another method that also uses the above animalRepository to return a specific entry by searching by id
    ```
4. Now we have our two get methods, but we still want a delete, edit, and create method. We will add those using the prompts below. Please note that this time we are asking for *2 different suggestions* to help increase the quality of what we are suggested:

Select one suggestion and delete the other.

    ```
    // Write another method that also uses the above animalRepository to delete and entry by specific id. Please provide two different solutions.
    ```

 
    ```
    // Write another method that also uses the above animalRepository to edit and entry by specific id. Please provide two different solutions. The entries in the animal database are structured as follows: 
    // public class Animal { @Id private Long id; private String name; private String description; }
    ```
 

    ```
    // Write another method that also uses the above animalRepository to create a new entry. Please provide two different solutions. The entries in the animal database are structured as follows: 
    // public class Animal { @Id private Long id; private String name; private String description; }
    ```

5. For reference this is what your _AnimalQueryService.java_ field may look like after running the commands above:

    ```
    package com.example.springboot.service;

    import com.example.springboot.entity.Animal;
    import com.example.springboot.repository.AnimalRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class AnimalQueryService {

        @Autowired
        private AnimalRepository animalRepository;

        public List<Animal> getAllAnimals() {
            return animalRepository.findAll();
        }

        public Optional<Animal> getAnimalById(Long id) {
            return animalRepository.findById(id);
        }

        public void deleteAnimalById(Long id) {
            animalRepository.deleteById(id);
        }

        public Optional<Animal> updateAnimalById(Long id, Animal updatedAnimal) {
            Optional<Animal> existingAnimal = animalRepository.findById(id);
            if (existingAnimal.isPresent()) {
                Animal animal = existingAnimal.get();
                animal.setName(updatedAnimal.getName());
                animal.setDescription(updatedAnimal.getDescription());
                return Optional.of(animalRepository.save(animal));
            } else {
                return Optional.empty();
            }
        }

        public Optional<Animal> createAnimal(Animal newAnimal) {
            Animal savedAnimal = animalRepository.save(newAnimal);
            return Optional.of(savedAnimal);
        }
    }
    ```

6. Now that we have a functioning query service we need to expose the actual route to use them. Lets then navigate to the _AnimalQuerycontroller.java_ (animal-hike/src/main/java/com/example/springboot/controller/AnimalQueryController.java) to do this next.

## Creating Our Routes

1. We next want to use the methods we created in our service to expose API endpoints in our controller. We will be creating a 1 to 1 pair of route to function as per whats in the service. Lets start with the _GetMapping_ methods:

    ```
    // write a mapping the uses the getAllAnimals function to return a list of all animal entries
    ```

    ```
    // write a mapping the uses the getAnimalById function to return a specific animal entry based on the provided id
    ```
2. Next lets add a route for our deleteAnimalById function using _DeleteMapping_:

    ```
    // write a delete mapping the uses the deleteAnimalById function to delete a specific animal entry based on the provided id
    ```

3. Lastly we will add routes for our updateAnimalById and createAnimal functions:

    ```
    // write a put mapping the uses the updateAnimalById function to edit a specific animal entry based on the provided id and updatedAnimal of type Animal. Only update the animal through the function call.
    ```

    ```
    // write a post mapping the uses the createAnimal function to create a new animal entry. Only create the animal entry through the function call.
    ```

4. Your _AnimalQueryController.java_ should look something like this when done:

    ```
    package com.example.springboot.controller;

    import org.springframework.web.bind.annotation.RestController;
    import com.example.springboot.entity.Animal;
    import com.example.springboot.service.AnimalQueryService;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.PutMapping;
    import org.springframework.web.bind.annotation.DeleteMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.ResponseBody;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RestController;
    import org.springframework.http.HttpStatus;


    import java.util.List;
    import java.util.Optional;

    @RestController
    public class AnimalQueryController {

        

        private final AnimalQueryService animalQueryService;

        public AnimalQueryController(AnimalQueryService animalQueryService) {
            this.animalQueryService = animalQueryService;
        }


        @GetMapping("/")
        public String index() {
            return "Greetings from Spring Boot!";
        }

        
        
        @GetMapping("/animals")
        public ResponseEntity<List<Animal>> getAllAnimals() {
            List<Animal> animals = animalQueryService.getAllAnimals();
            return ResponseEntity.ok(animals);
        }

        @GetMapping("/animals/{id}")
        public ResponseEntity<Animal> getAnimalById(@PathVariable("id") Long id) {
            Optional<Animal> animal = animalQueryService.getAnimalById(id);
            return animal.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        }

        @DeleteMapping("/animals/{id}")
        public ResponseEntity<Void> deleteAnimalById(@PathVariable("id") Long id) {
            animalQueryService.deleteAnimalById(id);
            return ResponseEntity.noContent().build();
        }

        @PutMapping("/animals/{id}")
        public ResponseEntity<Animal> updateAnimalById(@PathVariable("id") Long id, @RequestBody Animal updatedAnimal) {
            Optional<Animal> animal = animalQueryService.updateAnimalById(id, updatedAnimal);
            return animal.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        }

        @PostMapping("/animals")
        public ResponseEntity<Animal> createAnimal(@RequestBody Animal newAnimal) {
            Animal createdAnimal = animalQueryService.createAnimal(newAnimal).orElseThrow(() -> new RuntimeException("Failed to create Animal"));;
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAnimal);
        }

    }
    ```

5. Use the left hand menu in our web ide to select **Source Control**, add a quick commit message, ensure it is pointing at our merge request branch then click commit.
6. Navigate back to GitLab and locate our open merge request. From here we should see that a pipeline has been kicked off to securely scan and build our code. Take a quick break while it runs.

## Parsing Results and Generating Unit Tests

1. Now that our merge request pipeline has run we can see that our _test_ job is still failing. Before merging our code into main we want to fix this. Navigate back to our web ide tab and locate the test folder. Take a look at what we currently have for the AnimalQueryControllerTest.java.
2. This isnt testing any of our new methods that we had generated with Duo. Lets navigate back to the _AnimalQueryController.java_ file and highlight the entire file. from here right click the highlighted code, hover over **GitLab Duo Chat**, then click **Generate Tests**.
3. This will then open a chat window where an output of our new test case should show up. Copy that new code and fully replace the code in _AnimalQueryControllerTest.java_. Lastly ensure that the below line has been added to our imports or the test will still fail:

    ```
    import com.example.springboot.controller.AnimalQueryController;
    ```

4. Here is an example output you can reference if you get stuck:

    ```
    import com.example.springboot.entity.Animal;
    import com.example.springboot.service.AnimalQueryService;
    import com.example.springboot.controller.AnimalQueryController;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.MockitoAnnotations;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;

    import java.util.Arrays;
    import java.util.List;
    import java.util.Optional;

    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.mockito.Mockito.*;

    class AnimalQueryControllerTest {

        @Mock
        private AnimalQueryService animalQueryService;

        @InjectMocks
        private AnimalQueryController animalQueryController;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testGetAllAnimals() {
            List<Animal> animals = Arrays.asList(
                    new Animal(1L, "Dog", "Mammal"),
                    new Animal(2L, "Cat", "Mammal")
            );
            when(animalQueryService.getAllAnimals()).thenReturn(animals);

            ResponseEntity<List<Animal>> response = animalQueryController.getAllAnimals();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(animals, response.getBody());
        }

        @Test
        void testGetAnimalById() {
            Animal animal = new Animal(1L, "Dog", "Mammal");
            when(animalQueryService.getAnimalById(1L)).thenReturn(Optional.of(animal));

            ResponseEntity<Animal> response = animalQueryController.getAnimalById(1L);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(animal, response.getBody());
        }

        @Test
        void testGetAnimalByIdNotFound() {
            when(animalQueryService.getAnimalById(1L)).thenReturn(Optional.empty());

            ResponseEntity<Animal> response = animalQueryController.getAnimalById(1L);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void testDeleteAnimalById() {
            ResponseEntity<Void> response = animalQueryController.deleteAnimalById(1L);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(animalQueryService, times(1)).deleteAnimalById(1L);
        }

        @Test
        void testUpdateAnimalById() {
            Animal updatedAnimal = new Animal(1L, "Dog", "Mammal");
            when(animalQueryService.updateAnimalById(1L, updatedAnimal)).thenReturn(Optional.of(updatedAnimal));

            ResponseEntity<Animal> response = animalQueryController.updateAnimalById(1L, updatedAnimal);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(updatedAnimal, response.getBody());
        }

        @Test
        void testUpdateAnimalByIdNotFound() {
            Animal updatedAnimal = new Animal(1L, "Dog", "Mammal");
            when(animalQueryService.updateAnimalById(1L, updatedAnimal)).thenReturn(Optional.empty());

            ResponseEntity<Animal> response = animalQueryController.updateAnimalById(1L, updatedAnimal);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void testCreateAnimal() {
            Animal newAnimal = new Animal(1L, "Dog", "Mammal");
            when(animalQueryService.createAnimal(newAnimal)).thenReturn(Optional.of(newAnimal));

            ResponseEntity<Animal> response = animalQueryController.createAnimal(newAnimal);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(newAnimal, response.getBody());
        }

        @Test
        void testCreateAnimalFailure() {
            Animal newAnimal = new Animal(1L, "Dog", "Mammal");
            when(animalQueryService.createAnimal(newAnimal)).thenReturn(Optional.empty());

            try {
                animalQueryController.createAnimal(newAnimal);
            } catch (RuntimeException e) {
                assertEquals("Failed to create Animal", e.getMessage());
            }
        }
    }
    ```
5. Now that our tests are added we can confirm that everything we created using Duo Chat is working! Lets use the left hand menu to click through **Source Control**, add a quick commit message, ensure it is still pointing at our merge request branch then click **Commit...**. A resulting popup should bring us back to the GitLab view.
6. Once again a merge request pipeline should kick off. We can take a short 5 minute break while it runs, and come back later expecting the test job to now succeed proving our generated code is working. 
7. Once we confirm the build,tests, and test job all pass we can go ahead and merge this code into main.

## Securing Our Application

1. Wait for the pipeline to fully run on main before moving forward.Once the pipeline has run we can use the left hand navigation menu to click through **Secure > Vulnerability report** to view the full security report.
2. First, click into any of the vulnerabilities present. Notice that there are a number of vulnerabilities, all of which GitLab will help you quickly fix through policies and the power of one platform.
3. Filter the report as follows:
    - `Status=All statuses`
    - `Activity=Still detected`
    - `Tool=SAST-kics`
4. Select the critical vulnerability. At the bottom of the vulnerability’s page, click the `Explain Vulnerability` button.
5. A popup will open on the right-hand side and you will get an explanation on what the vulnerability is using [GitLab's Explain This Vulnerability](https://docs.gitlab.com/ee/user/application_security/vulnerabilities/index.html#explaining-a-vulnerability) functionality.
6. We can also easily resolve this vulnerability. Go ahead and click **Resolve with AI**. This will then open up a new Merge Request that fixes our vulnerability. Click the **Changes** tab to view the fix that we are bringing in.

## Conclusion & Next Steps

This guide focused just on GitLab Duo Chat and Code Suggestions, but you have access to many more [AI features](https://docs.gitlab.com/ee/user/ai_features.html) through this space. Feel free to take some time testing out some of the other features like issue summary, explain this code, and more for the duration of your POV.