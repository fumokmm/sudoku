import groovy.transform.ToString

def inputData = new File(args[0]).text

def logger = new File("./${args[0]}.log").newPrintWriter()

def cellPool = []
def data = inputData.trim().split(/\n/).collect{
    def lineNums = it.trim().grep(~/[_1-9]/)
    if (lineNums.empty) return null
    
    lineNums.collect{
        def c
        if (it == '_') {
            c = new Cell(num: 0)
            cellPool << c
        } else {
            c = new Cell(num: it.toInteger())
            cellPool << c
            c
        }
        c
    }
}.findAll{ it != null }

// セル情報
class Cell {
    def num
    def guessNums = (1..9).toList()
    /** 数字を更新済み(0→数字)へかどうか */
    def updated = false
    
    // 確定させる
    def fix() {
        if (this.num == 0) {
            // 候補が残り1つとなっている場合は、そちらで確定
            if (this.guessNums.size() == 1) {
                this.num = this.guessNums.first()
                this.guessNums.clear()
                this.updated = true
                return
            }
        }
    }
    @Override String toString() {
        "[$num]"
    }
}

abstract class Analyzer {
    def name
    def dt
    def logger

    /** 現在の確定状態から、guessNumsを計算する */
    def analyze() {
        this.dt.each { d ->
            // 未確定の場合、スキップ
            if (d.num == 0) {
                return

            // 確定済みの場合、予測をクリア
            } else {
                d.guessNums.clear()
            }

            // 確定済み情報を他のセルへ反映
            // logger.println "$this.name 確定済み情報を他のセルへ反映"
            this.dt.each {
                it.guessNums = it.guessNums - d.num
            }
        }
    }

    // 確定させる
    def fix() {
        // 範囲のセル中の候補中、1つしか登場しない数字を取得し、確定させていく
        def onlyOneNums = dt.guessNums.flatten().countBy{ it }.findAll{ it.value == 1 }.keySet()
        onlyOneNums.each{ oon ->
            for (def d in dt.findAll{ it.num == 0 }) {
                if (d.guessNums.contains(oon)) {
                    d.num = oon
                    d.guessNums.clear()
                    d.updated = true
                }
            }
        }
    }

    def printData() {
        logger.println "${this.name}${this.dt}"
    }
}

/** 横解析 */
class HorizontalAnalyzer extends Analyzer {
    HorizontalAnalyzer(name, logger, data, int row) {
        this.name = name
        this.logger = logger
        this.dt = data[row]
    }
}

/** 縦解析 */
class VerticalAnalyzer extends Analyzer {
    VerticalAnalyzer(name, logger, data, int columns) {
        this.name = name
        this.logger = logger
        this.dt = data.collect{ it[columns] }
    }
}

/** ブロック解析 */
class BlockAnalyzer extends Analyzer {
    BlockAnalyzer(name, logger, data, int blockNum) {
        this.name = name
        this.logger = logger
        int r = ((blockNum / 3) as int) * 3
        int c = ((blockNum % 3) as int) * 3
        this.dt = data[r][c..(c + 2)] + data[r + 1][c..(c + 2)] + data[r + 2][c..(c + 2)]
    }
}

def analyzers = []
9.times{ analyzers << new HorizontalAnalyzer("H$it", logger, data, it) }
9.times{ analyzers << new VerticalAnalyzer("V$it", logger, data, it) }
9.times{ analyzers << new BlockAnalyzer("B$it", logger, data, it) }

// 未確定の個数をチェック
def checkEmptyCount = {
    cellPool.findAll{ it.num == 0}.size()
}

// 以下、結果が出るまで解析ループ
def count = checkEmptyCount()
def step = 0
while (true) {
    step++
    logger.println "step${step} 未確定件数: ${count}"
    // 解析実施と確定
    analyzers.each{
        it.analyze()
        it.fix()
    }
    // 解析後確定
    cellPool.each{ it.fix() }
    def newCount = checkEmptyCount()
    
    if (newCount == 0) {
        logger.println "解析完了"
        break

    } else {
        // 解析後確定数が変わらない場合
        if (count == newCount) {
            logger.println "解析不可能"
            break

        } else {
            count = newCount
        }
    }
}
logger.close()

// 結果出力
println ''
println '=========== Result ==========='
cellPool.eachWithIndex{ c, index ->
    def idx = index + 1
    print "${c.updated ? '*' : ' '}${c.num == 0 ? '_' : c.num }"
    if (idx % 9 != 0) print ' '
    if (idx % 3 == 0 && idx % 9 != 0) print '| '
    if (idx % 9 == 0) println ''
    if (idx % 27 == 0 && idx % 81 != 0) println '---------+----------+---------'
}
println '==============================' 
