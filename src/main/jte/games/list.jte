@param java.util.List<org.example.gameslibrary.dto.GameDto> games

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Games Library</title>
</head>
<body>
<h1>Games Library</h1>
<a href="/games/create">Add New Game</a>

<table>
    <thead>
    <tr>
        <th>Title</th>
        <th>Description</th>
        <th>Release Date</th>
        <th>Price</th>
        <th>Categories</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    @for(var game : games)
        <tr>
            <td>${game.getTitle()}</td>
            <td>${game.getDescription()}</td>
            <td>${game.getReleaseDate() != null ? game.getReleaseDate().toString() : ""}</td>
            <td>${game.getPrice() != null ? game.getPrice().toString() : ""}</td>
            <td>${String.join(", ", game.getCategories())}</td>
            <td>
                <a href="/games/edit/${game.getId()}">Edit</a>
                <form action="/games/delete/${game.getId()}" method="post" style="display:inline;">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
    @endfor
    </tbody>
</table>
</body>
</html>