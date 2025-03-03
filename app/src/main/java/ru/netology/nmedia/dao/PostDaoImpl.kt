package ru.netology.nmedia.dao


import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase): PostDao{

    companion object{

        val DDl = """
            CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLOMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLOMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLOMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLOMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLOMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLOMN_LIKES} INTEGER NOT NULL DEFAULT 0
            );
           
           """.trimIndent()
            
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLOMN_ID = "id"
        const val COLOMN_AUTHOR = "author"
        const val COLOMN_CONTENT = "content"
        const val COLOMN_PUBLISHED = "published"
        const val COLOMN_LIKED_BY_ME = "likedByMe"
        const val COLOMN_LIKES = "likes"
        val ALL_COLOMNS = arrayOf(
            COLOMN_ID,
            COLOMN_AUTHOR,
            COLOMN_CONTENT,
            COLOMN_PUBLISHED,
            COLOMN_LIKED_BY_ME,
            COLOMN_LIKES
        )
    }
    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLOMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLOMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostColumns.COLOMN_AUTHOR, "Me")
            put(PostColumns.COLOMN_CONTENT, post.content)
            put(PostColumns.COLOMN_PUBLISHED, "Now")
        }
        val id = if (post.id != 0L) {
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.COLOMN_ID} =?",
                arrayOf(post.id.toString()),
            )
            post.id
        } else {
            db.insert(PostColumns.TABLE, null, values)
        }
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLOMNS,
            "${PostColumns.COLOMN_ID} =?",
            arrayOf(id.toString()),
            null,
            null,
            null
            ). use {
                it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLOMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun map(cursor: Cursor): Post {
        with(cursor){
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLOMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLOMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLOMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLOMN_PUBLISHED)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLOMN_LIKED_BY_ME)) != 0,
                likes = getInt(getColumnIndexOrThrow(PostColumns.COLOMN_LIKES)),
            )
        }
    }

}