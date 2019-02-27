package org.sixpence.zkdash.view.component

import java.awt.{Dimension, Image}
import java.awt.event.{ActionEvent, ActionListener, MouseEvent, MouseListener}

import javax.swing.event._
import javax.swing.{ImageIcon, _}
import javax.swing.tree.{DefaultMutableTreeNode, TreeModel, TreePath}
import org.sixpence.zkdash.command.{FetchDataCommand, LsCommand, PathData}

/**
  * @author geksong
  * Created by geksong on 2019/2/23.
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
    val iconPath = this.getClass.getClassLoader.getResource("zkdashicon.png").getFile
    val icon = new ImageIcon(iconPath)
    icon.setImage(icon.getImage.getScaledInstance(80, 80, Image.SCALE_DEFAULT))
    val searchPattern = JOptionPane.showInputDialog(source, "Search for", "Search", JOptionPane.QUESTION_MESSAGE, icon, None.orNull, "")
    Option(searchPattern).map(a => a.toString).foreach(a => {
      val treeModel = source.getModel
      val root = treeModel.getRoot.asInstanceOf[DefaultMutableTreeNode]
      val rootPath = new TreePath(root)
      val searchedPath = findInPath(treeModel, rootPath, a)
      Option(searchedPath).foreach(a => {
        source.setSelectionPath(a)
        source.scrollPathToVisible(a)
      })
    })

    def findInPath(treeModel: TreeModel, treePath: TreePath, str: String): TreePath = {
      val objOp = Option(treePath.getLastPathComponent)
      if(objOp.isEmpty) {
        objOp.orNull
      }
      val obj = objOp.get
      val path = obj.toString
      if(path == s"/${str}".replaceAll("//", "/")) {
        treePath
      }else {
        val chCount = treeModel.getChildCount(obj)
        val lsSch = (0 until chCount).map(i => {
          val ch = treeModel.getChild(obj, i)
          val chPath = treePath.pathByAddingChild(ch)
          val serChPath = findInPath(treeModel, chPath, str)
          Option(serChPath)
        }).filter(_.nonEmpty).map(_.get)
        if(lsSch.isEmpty) None.orNull else lsSch.head
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
