# legco-hansard-pdf-extractor
Legco Hansard PDF Extractor converts legco hansard PDF into JSON.

##  Usage
1. Download the latest jar from [releases](https://github.com/g0vhk-io/legco-hansard-pdf-extractor/releases).
2. Run the following command
```bash
java -jar hansard-parser.jar https://www.legco.gov.hk/yr17-18/chinese/counmtg/floor/cm20171025-confirm-ec.pdf
```
### Development
1. Checkout the latest source code 
```bash
git clone git@github.com:g0vhk-io/legco-hansard-pdf-extractor.git
```
2. Build with gradle
```bash
./gradlew build
```

3. Debug / Running
```bash
./gradlew run -Dexec.args=https://www.legco.gov.hk/yr12-13/chinese/counmtg/floor/cm1212-confirm-ec.pdf
```
### Release
1. Build one big jar
```bash
./gradlew  shadowJar
```
### Contributing
Please free feel to open an issue or PR. 
 
### Author
- Ho Wa Wong (info@g0vhk.io)
