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
  // 監視するセルを宣言
  def cells = []
  // 決定済みの値
  def fixeds = []
  def allVal = 1..cells.size

  // 監視している全てのセルで決定済みの値を取得する
  def fix() {
    fixeds = cells.findAll(cells.fixed != 0)
  }
      
  // 監視しているセルに対して候補を埋める
  def calc() {
    cells.findAll(cells.fixed == 0).each() {
      // TODO candidateが存在する場合はAnd演算
      // 候補を決定
      it.candidate = allVal - fixeds
    }
  }
}
