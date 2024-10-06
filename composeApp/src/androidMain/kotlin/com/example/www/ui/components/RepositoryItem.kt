package com.example.www.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.www.R
import com.example.www.domain.model.Repository
import com.example.www.ui.theme.accentPrimaryLight
import com.example.www.ui.theme.backgroundsPrimaryLight
import com.example.www.ui.theme.foregroundsSecondaryLight

@Composable
fun RepositoryItem(
    repo: Repository,
    isStarred: Boolean,
    onFavoriteClicked: (id: String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundsPrimaryLight)
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                contentDescription = "Owner Image",
                modifier = Modifier.size(80.dp),
                model = repo.ownerImage,
                placeholder = painterResource(R.drawable.ic_person)
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = repo.name ?: stringResource(R.string.na_label),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
                Text(
                    text = repo.ownerLogin ?: stringResource(R.string.na_label),
                    style = MaterialTheme.typography.bodySmall
                )
                Row(
                    modifier = Modifier.padding(all = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "Star icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "${repo.starsCount}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_fork),
                        contentDescription = "Fork icon",
                        modifier = Modifier
                            .size(24.dp)
                            .padding(start = 4.dp)
                    )
                    Text(
                        text = "${repo.forksCount}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            //TODO-ADD FAVORITE
            Image(
                painter = painterResource(id = R.drawable.ic_favorite),
                colorFilter = if (isStarred) {
                    ColorFilter.tint(accentPrimaryLight)
                } else {
                    ColorFilter.tint(foregroundsSecondaryLight)
                },
                contentDescription = "Favorite feature icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp)
                    .clickable(onClick = {
                        repo.nodeId.let { onFavoriteClicked.invoke(it) }
                    })
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RepositoryCardPreview() {
    val isStarred = remember {
        mutableStateOf(true)
    }
    RepositoryItem(
        repo = Repository(
            nodeId = "asdfasdf",
            name = "ComposeApp",
            repoUrl = "https://github.com/rohlik-group/compose-app",
            starsCount = 4500,
            forksCount = 530,
            ownerLogin = "Rohlik Group",
            ownerImage = "https://avatars.githubusercontent.com/u/1?v=4",
            isStarred = false
        ),
        isStarred = false
    ) {

    }
}