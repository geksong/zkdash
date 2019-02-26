package org.sixpence.zkdash.view.component

import java.awt.{Dimension, Image}
import java.awt.event.{ActionEvent, ActionListener, MouseEvent, MouseListener}

import javax.swing.event._
import javax.swing.{ImageIcon, _}
import javax.swing.tree.{DefaultMutableTreeNode, TreeModel, TreePath}
import org.sixpence.zkdash.command.{FetchDataCommand, LsCommand, PathData}

/**
  *
  * Created by bianshi on 2019/2/23.
  */
class NodeComponent(lsCommand: LsCommand, fetchDataCommand: FetchDataCommand, cf: PathData => Unit) {
  private[this] val top = new DefaultMutableTreeNode("/")

  def build(): JScrollPane = {
    val tree = new JTree(top)
    tree.addTreeSelectionListener(e => {
      val selNode: DefaultMutableTreeNode = e.getPath.getLastPathComponent.asInstanceOf[DefaultMutableTreeNode]
      // if has no children
      val pa = selNode.getUserObjectPath.mkString("").replaceAll("//", "/")
      if(selNode.isLeaf) {
        lsCommand.execute(pa).subscribe(a => a.foreach(b => {
          val nd = new DefaultMutableTreeNode(s"/${b}")
          selNode.add(nd)
        }))
      }
      fetchDataCommand.execute(pa).subscribe(a => cf(a))
    })

    tree.addMouseListener(new TreeMouseListener(tree))

    val treeScroll = new JScrollPane(tree)
    treeScroll.setPreferredSize(new Dimension(250, 400))
    treeScroll.setMinimumSize(new Dimension(250, 200))
    treeScroll
  }
}

class TreeMouseListener(source: JTree) extends MouseListener {
  val popupMenu = new JPopupMenu()
  val searchMenu = new JMenuItem("Search")

  searchMenu.addActionListener(e => {
    val selNode = source.getLastSelectedPathComponent.asInstanceOf[DefaultMutableTreeNode]
    println(s"select node ${selNode}")
    //JOptionPane.showMessageDialog(source, "What are you doing now!!!", "Check check check", JOptionPane.QUESTION_MESSAGE)
    val iconPath = this.getClass.getClassLoader.getResource("zkdashicon.png").getFile
    val icon = new ImageIcon(iconPath)
    icon.setImage(icon.getImage.getScaledInstance(80, 80, Image.SCALE_DEFAULT))
    val searchPattern = JOptionPane.showInputDialog(source, "Search for", "Search", JOptionPane.QUESTION_MESSAGE, icon, null, "")
    Option(searchPattern).map(a => a.toString).foreach(a => {
      val treeModel = source.getModel
      val root = treeModel.getRoot.asInstanceOf[DefaultMutableTreeNode]
      val rootPath = new TreePath(root)
      val searchedPath = findInPath(treeModel, rootPath, a)
      if(null != searchedPath) {
        source.setSelectionPath(searchedPath)
        source.scrollPathToVisible(searchedPath)
      }
    })

    def findInPath(treeModel: TreeModel, treePath: TreePath, str: String): TreePath = {
      val obj = treePath.getLastPathComponent
      if(null == obj) {
        return null
      }
      val path = obj.toString
      if(path == s"/${str}".replaceAll("//", "/")) {
        return treePath
      }else {
        val chCount = treeModel.getChildCount(obj)
        for(i <- 0 until chCount) {
          val ch = treeModel.getChild(obj, i)
          val chPath = treePath.pathByAddingChild(ch)
          val serChPath = findInPath(treeModel, chPath, str)
          if(null != serChPath) return serChPath
        }
        return null
      }
    }
  })

  popupMenu.add(searchMenu)

  override def mouseClicked(e: MouseEvent): Unit = {}

  override def mousePressed(e: MouseEvent): Unit = {
    val path = source.getPathForLocation(e.getX, e.getY)
    source.setSelectionPath(path)
    if (e.getButton == 3) popupMenu.show(source, e.getX, e.getY)
  }

  override def mouseReleased(e: MouseEvent): Unit = {}

  override def mouseEntered(e: MouseEvent): Unit = {}

  override def mouseExited(e: MouseEvent): Unit = {}
}
