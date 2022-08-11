package com.gdu.command.ui.setting;

import com.gdu.command.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 树形菜单Node类
 */
public abstract class Node<T> {
    private int _level = -1; //当前节点的层级，初始值-1
    private List<Node> _childrenList = new ArrayList<>(); //所以孩子的节点
    private Node _parent; //父节点
    private int _icon; //图标icon
    private boolean isExpand = false; //当前状态是否展开

    public abstract T get_id(); //得到当前节点id
    public abstract T get_parentId(); //得到当前节点的父id
    public abstract String get_label(); //要显示的内容
    public abstract boolean parent(Node dest); //判断当前节点是否是dest的父节点
    public abstract boolean child(Node dest); //判断当前节点是否是dest的子节点
    public abstract String deptCode(); //部门id

    public int get_level() {
        if (_level == -1) {
            //如果是-1的话就递归获取
            //因为是树形结构，所以此处想要得到当前节点的层级，这里必须递归调用，
            //但是递归效率低，每次都递归获取严重影响性能，所以把第一次得到的结果保存起来
            int level = _parent == null ? 1 : _parent.get_level() + 1;
            _level = level;
            return _level;
        }
        return _level;
    }

    public void set_level(int _level) {
        this._level = _level;
    }

    public List<Node> get_childrenList() {
        return _childrenList;
    }

    public void set_childrenList(List<Node> _childrenList) {
        this._childrenList = _childrenList;
    }

    public Node get_parent() {
        return _parent;
    }

    public void set_parent(Node _parent) {
        this._parent = _parent;
    }

    public int get_icon() {
        return _icon;
    }

    public void set_icon(int _icon) {
        this._icon = _icon;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean expand) {
        isExpand = expand;
        if (isExpand) {
            _icon = R.mipmap.drop_down_unselected_icon;
        } else {
            _icon = R.mipmap.drop_down_selected_icon;
        }
    }

    public boolean isRoot() {
        return _parent == null;
    }

    public boolean isLeaf() {
        return _childrenList == null;
    }
}
