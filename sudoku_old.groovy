// 数独をマルチスレッドで解く
 
class Cell {
  // ポジション
  int x, y
  // 決定した数
  int fixed
  // 候補の数のリスト
  def candidate = []
}

class Observer {
  // 観察するセルを宣言
  def cells = []
  // TOOD 見直すかも
  // ルール
  def rule = {}
  // 決定済みの値
  def fixeds = []

  // TODO 見直すかも
  // 観察者生成
  def create(def cells, def rule) {
    this.cells = cells
    this.rule = rule
  }

  // 観察している全てのセルで決定済みの値を取得する
  def fix() {
    fixeds = cells.findAll(cells.fixed != 0)
  }
      
  // TODO ルールをクロージャで受け取り、担当とするセルから候補を決める
  def updateCandidate4Rule() {
    rule()
  }

  // 観察しているセルに対して候補を埋める
  def updateCandidate() {
    def allVal = 1..cells.size
    cells.findAll(cells.fixed == 0).each() {
      // TODO candidateが存在する場合はAnd演算
      // 候補を決定
      it.candidate = allVal - fixeds
    }
  }
}



// ここからMain！
// 問題はとりあえず http://goo.gl/JgIcp の
//「ナンプレ超初級問題１」固定で。

// 全セルを定義
def allCells = [
  new Cell(x: 0, y: 0, fixed: 0, candidate: []),
  new Cell(x: 1, y: 0, fixed: 0, candidate: []),
  new Cell(x: 2, y: 0, fixed: 4, candidate: []),
  new Cell(x: 3, y: 0, fixed: 0, candidate: []),
  new Cell(x: 0, y: 1, fixed: 0, candidate: []),
  new Cell(x: 1, y: 1, fixed: 0, candidate: []),
  new Cell(x: 2, y: 1, fixed: 0, candidate: []),
  new Cell(x: 3, y: 1, fixed: 1, candidate: []),
  new Cell(x: 0, y: 2, fixed: 4, candidate: []),
  new Cell(x: 1, y: 2, fixed: 0, candidate: []),
  new Cell(x: 2, y: 2, fixed: 0, candidate: []),
  new Cell(x: 3, y: 2, fixed: 0, candidate: []),
  new Cell(x: 0, y: 3, fixed: 2, candidate: []),
  new Cell(x: 1, y: 3, fixed: 1, candidate: []),
  new Cell(x: 2, y: 3, fixed: 0, candidate: []),
  new Cell(x: 3, y: 3, fixed: 0, candidate: [])
]

/** 結果出力 */
def output(cells) {
  (0..3).each { y ->
    (0..3).each { x ->
      print( cells.find { it.x == x && it.y == y }.fixed )
    }
    println()
  }
}

/**
 * 終了判定
 * @return true: 終了してよい, false: まだ終わらせるわけにはいかない
 */
boolean judge(cells) {
  // すべてのcellがfixになったなら終了
  cells.every {
    synchronized(it) {
      return it.fixed > 0
    }
  }
}

println '----- 開始 -----'
output(allCells)

while (true) {
  // ここで解く


  if (judge(allCells)) {
    break
  }
}

println '----- 終了 -----'
output(allCells)
