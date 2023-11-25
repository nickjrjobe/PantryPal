# cse-110-project-team-40

## Basic Usage 
### Setup config.json
#### Setup API Key
Replace the key value in the supplied config.json file
#### Setup Database
Replace MongoDBURI with your MongoDBURI, and MongoDBDatabase with your MongoDBDatabase
### Setup (Linux/Mac)
1. `$ chmod +x setup_lib.sh`
2. `$ bash setup_lib.sh`

### Setup (Mac)
1. `$ chmod +x setup_lib_mac.sh`
2. `$ bash setup_lib_mac.sh`

**TODO** create a mac script that installs the right JavaFX library

### running
#### To test app/ui (must run both server and app at same time) 
- app: `$ make && make run`
- server: `$ make && make server`
#### To run tests
- client test: `$ make && make test`
- server test: `$ make && make servertest`
- lint: `$ make lint`

### Install required libraries (Windows)
https://drive.google.com/drive/folders/15VwRpML39KdcdcyukAYt2yKiyjcorLWI?usp=drive_link


## Style
- we follow the ![Google Style Guide](https://google.github.io/styleguide/javaguide.html)
- indentation is 2 spaces
#### To auto format
1. install google formatter (uncomment line in setup_lib.sh)
2. run `make format`

