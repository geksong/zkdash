package org.sixpence.zkdash.view.component

import java.awt._
import java.awt.event._

import javax.swing.{ImageIcon, _}
import javax.swing.tree.{DefaultMutableTreeNode, TreeModel, TreePath}
import org.sixpence.zkdash.command.{FetchDataCommand, LsCommand, PathData}
import org.sixpence.zkdash.view.component.builder.GridBagConstraintsBuilder
import reactor.core.publisher.Mono

/**
  * @author geksong
  * Created by geksong on 2019/2/23.
  */
class NodeComponent(lsCommand: LsCommand, fetchDataCommand: FetchDataCommand, cf: PathData => Unit) {
  private[this] val top = new DefaultMutableTreeNode("/")

  def build(): Mono[JPanel] = {
    Mono.create(sink => {

      val allPan = new JPanel(new GridBagLayout)
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

      val treeScroll = new JScrollPane(tree)
      treeScroll.setPreferredSize(new Dimension(250, 400))
      treeScroll.setMinimumSize(new Dimension(250, 200))

      val searchPan = new JPanel()
      searchPan.setLayout(new BoxLayout(searchPan, BoxLayout.X_AXIS))
      val textField = new JTextField("")
      textField.enableInputMethods(true)
      textField.addKeyListener(new TreeSearchListener(tree, textField))

      val sechBut = new JButton("Search")
      sechBut.addActionListener(new TreeSearchListener(tree, textField))
      searchPan.add(textField)
      searchPan.add(sechBut)

      allPan.add(searchPan, GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridheight(1).weightx(1)
          .gridx(0).gridy(0).build())
      allPan.add(treeScroll, GridBagConstraintsBuilder().fill(GridBagConstraints.BOTH).gridheight(1)
        .weightx(1).weighty(1).gridx(0).gridy(1).build())
      sink.success(allPan)
    })
  }
}


class TreeSearchListener(source: JTree, searchField: JTextField) extends KeyListener with ActionListener {
  override def keyTyped(e: KeyEvent): Unit = {}

  override def keyPressed(e: KeyEvent): Unit = {
    if(e.getKeyCode == KeyEvent.VK_ENTER) handleSearch()
  }

  override def keyReleased(e: KeyEvent): Unit = {}

  override def actionPerformed(e: ActionEvent): Unit = {
    handleSearch()
  }

  def handleSearch() = {
    val seachStr = searchField.getText
    Option(seachStr).foreach(a => {
      val treeModel = source.getModel
      val root = treeModel.getRoot.asInstanceOf[DefaultMutableTreeNode]
      val rootPath = new TreePath(root)
      val searchedPath = findInPath(treeModel, rootPath, a)
      Option(searchedPath).foreach(a => {
        source.setSelectionPath(a)
        source.scrollPathToVisible(a)
      })
    })
  }

  def findInPath(treeModel: TreeModel, treePath: TreePath, str: String): TreePath = {
    val objOp = Option(treePath.getLastPathComponent)
    objOp.map(obj => {
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
    }).getOrElse(None.orNull)
  }
}
