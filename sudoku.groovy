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
}



// ここからMain！
// 問題はとりあえず
// http://bit.ly/15caX31
// の「ナンプレ超初級問題１」固定で。

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

println '----- 開始 -----'
output(allCells)

// ここで解く

println '----- 終了 -----'
output(allCells)
