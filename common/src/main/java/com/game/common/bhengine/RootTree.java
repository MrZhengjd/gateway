package com.game.common.bhengine;


import java.util.List;
import java.util.Map;

/**
 * @author zheng
 */
public class RootTree extends BaseBTree  implements CompositeBTree{
    private CompositeBTree root;

    @Override
    public void addBTree(CompositeBTree bTree) {
        root.addBTree(bTree);
    }

    @Override
    public void removeTree(CompositeBTree bTree) {
        root.removeTree(bTree);
    }

    @Override
    public List<CompositeBTree> getChildrens() {
        return root.getChildrens();
    }

    public RootTree(CompositeBTree root) {
        super();
        this.root = root;
    }

    public EStatus handleRequest(Map<String ,Object> request){
        return root.handleRequest(request);
    }

}
