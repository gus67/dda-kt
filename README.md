Name | Academy | score 
- | :-: | -: 
Harry Potter | Gryffindor| 90 
Hermione Granger | Gryffindor | 100 
Draco Malfoy | Slytherin | 90

``` kotlin
fun traverseFileTree() {
    val systemDir = File("/Users/guxichang/monitor")
    val fileTree: FileTreeWalk = systemDir.walk()
    fileTree.maxDepth(10)
            .filter { it.isFile }
            .filter { it.extension != "COMPLETE" }
            .forEach(::println)

}