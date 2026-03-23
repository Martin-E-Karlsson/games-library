@param java.util.List<org.example.gameslibrary.dto.GameDto> games
@param int currentPage
@param int totalPages
@param String title = null
@param String category = null

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Games Library</title>
</head>
<body>
<h1>Games Library</h1>
<a href="/games/create">Add New Game</a>

<h2>Filter</h2>
<form action="/games" method="get">
    <div>
        <label for="title">Title:</label>
        <input type="text" id="title" name="title" value="${title != null ? title : ""}">
    </div>
    <div>
        <label for="category">Category:</label>
        <input type="text" id="category" name="category" value="${category != null ? category : ""}">
    </div>
    <button type="submit">Search</button>
    <a href="/games">Clear</a>
</form>

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

<div>
    @if(currentPage > 0)
        <a href="/games?page=${currentPage - 1}${title != null ? "&title=" + title : ""}${category != null ? "&category=" + category : ""}">Previous</a>
    @endif

    <span>Page ${currentPage + 1} of ${totalPages}</span>

    @if(currentPage < totalPages - 1)
        <a href="/games?page=${currentPage + 1}${title != null ? "&title=" + title : ""}${category != null ? "&category=" + category : ""}">Next</a>
    @endif
</div>
</body>
</html>