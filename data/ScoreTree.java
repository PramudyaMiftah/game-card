package data;

public class ScoreTree {
    private ScoreNode root;

    public void tambah(String nama, int skor) {
        root = tambahRecursive(root, nama, skor);
    }

    private ScoreNode tambahRecursive(ScoreNode current, String nama, int skor) {
        if (current == null) return new ScoreNode(nama, skor);
        if (skor < current.skor) current.kiri = tambahRecursive(current.kiri, nama, skor);
        else current.kanan = tambahRecursive(current.kanan, nama, skor);
        return current;
    }

    public void inorder() {
        inorderTraversal(root);
    }

    private void inorderTraversal(ScoreNode node) {
        if (node != null) {
            inorderTraversal(node.kiri);
            System.out.println(node.nama + " : " + node.skor);
            inorderTraversal(node.kanan);
        }
    }
}