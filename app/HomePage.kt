@Composable
fun HomePage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCE043)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Página Inicial",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
