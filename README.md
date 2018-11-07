# Retro MP3 Recorder

Implemented by Oleksii Kuropiatnyk

### Implementation choice

Retro MP3 Recorder:
 - records audio from mic
 - outputs in MP3 format
 - user may select output Bit and Sample rate
 - user may then share audio file

Application is implemented in a highly flexible
and maintainable approach.

Architecture is split into layers:

1. Business logic in **UseCase** layer. **UseCase** takes data from **IO** layer and puts it into **Repo**, or vice versa
2. Data maintenance in **Repo** layer
3. **Interactor** layer - executes **Action** on **UseCase** and subscribes to **Repo**. Then emits **Result**
5. **Mapper** layer  maps **Result** into **ViewModel**
4. **View** layer is agnostic about the Business Logic and IO. It does 2 things: - send **Actions** and render **ViewModel**
6. **IO** layer
7. **Navigation** layer (missing - not needed for current app)

With this approach **UseCase**, **IO** and **Repo** can be shared across screens.
All layers except **IO** and **View** are Android agnostic - they are fully Unit and even Integration testable.
The data flow is uni directional.

**Navigation** layer is also intended but not needed for current app.

The components are combined together with **Dagger2 DI**.

The data flow is controlled with **RxJava2** functional streams.

The unit tests cover **UseCases**, **Interactors** and **Mappers**. They can be found in **test** folder. The app is also integratable into **Travis CI**.
