// 数独をマルチスレッドで解く
 
class Cell {
  // ポジション
  int x, y
  // 決定した数
  int fixed
  // 候補の数のリスト
  def candidate = []
}
