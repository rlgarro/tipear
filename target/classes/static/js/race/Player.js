class Player {

    constructor(id = null) {
        this.name = null;
        this.isReady = false;
        this.id = id;
    }

    setName(name) {
        this.name = name;
    }

    getName() {
        return this.name;
    }

    setReady(ready) {
        this.isReady = ready;
    }

    playerIsReady() {
        return this.isReady;
    }

    setId(id) {
        this.id = id;
    }

    getId() {
        return this.id;
    }

}