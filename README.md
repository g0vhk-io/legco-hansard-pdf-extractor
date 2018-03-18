# legco-hansard-pdf-extractor
Legco Hansard PDF Extractor converts legco hansard PDF into JSON.

##  Usage
1. Download the latest jar from [releases](https://github.com/g0vhk-io/legco-hansard-pdf-extractor/releases).
2. Run the following command
```bash
java -jar hansard-parser.jar https://www.legco.gov.hk/yr17-18/chinese/counmtg/floor/cm20171025-confirm-ec.pdf
```
## Sample JSON
```javascript
{
    "membersPresent": [
        "主席曾鈺成議員, G.B.S., J.P. THE PRESIDENT THE HONOURABLE JASPER TSANG YOK-SING, G.B.S., J.P."
    ],
    "membersAbsent": [
        "涂謹申議員 THE HONOURABLE JAMES TO KUN-SUN"
    ],
    "publicOfficersAttending": [
        "民政事務局局長曾德成先生, G.B.S., J.P. THE HONOURABLE TSANG TAK-SING, G.B.S., J.P. SECRETARY FOR HOME AFFAIRS"
    ],
    "clerksInAttendance": ["助理秘書長梁慶儀女士 MISS ODELIA LEUNG HING-YEE, ASSISTANT SECRETARY GENERAL"],
    "speeches": [{
        "title": "全委會主席",
        "content": "早晨，全體委員會繼續審議《2015年撥款條例草案》的附表，現在繼續進行第6項辯論。 \n 陳家洛議員，請發言。",
        "sequence": 1,
        "bookmark": "SP_LC_CM_00008"
    }],
    "date": "2015-5-15",
    "url": "https://www.legco.gov.hk/yr14-15/chinese/counmtg/floor/cm20150515-confirm-ec.pdf"
}
```

## Development
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
## Release
1. Build one big jar
```bash
./gradlew  shadowJar
```
## Contributing
Please free feel to open an issue or PR. 
 
## Author
- Ho Wa Wong (info@g0vhk.io)
