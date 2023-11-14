package com.example.mobile_project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project.adapters.PostAdapter;
import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.Commentaire;
import com.example.mobile_project.entity.Post;
import com.example.mobile_project.entity.PostWithCommentaires;
import com.example.mobile_project.entity.User;
import com.example.mobile_project.entity.UserWithPosts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<UserWithPosts> postList; // Utilisez cette liste pour afficher les publications

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialiser le RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postList = new ArrayList<UserWithPosts>(); // Utilisez la liste des publications
        postAdapter = new PostAdapter(postList); // Utilisez la liste des publications
        recyclerView.setAdapter(postAdapter);

        // Charger les données depuis Room
        loadPostsFromRoom();

        return view;
    }

    private void loadPostsFromRoom() {
        new AsyncTask<Void, Void, List<UserWithPosts>>() {
            @Override
            protected List<UserWithPosts> doInBackground(Void... voids) {
                // Récupérez la liste des publications directement depuis la base de données Room
                return AppDatabase.getAppDatabase(requireContext()).postDao().getUsersWithPosts();
            }

            @Override
            protected void onPostExecute(List<UserWithPosts> userWithPostsList) {
                super.onPostExecute(userWithPostsList);

                if (userWithPostsList != null) {
                    for (UserWithPosts userWithPosts : userWithPostsList) {
                        User user = userWithPosts.user;
                        List<Post> posts = userWithPosts.posts;

                        if (posts != null && !posts.isEmpty()) {
                            for (Post post : posts) {
                                // Ajoutez des déclarations d'impression pour afficher les données dans les logs
                                Log.d("UserWithPosts", "User: " + user.getUserName());
                                Log.d("UserWithPosts", "Post Title: " + post.getTitle());
                                Log.d("UserWithPosts", "Post Description: " + post.getDescription());
                                // Ajoutez d'autres déclarations d'impression pour les autres attributs du Post
                                postList.add(userWithPosts);
                            }
                            // Ajoutez toutes les publications de cet utilisateur à la liste principale
                            ;
                        } else {
                            Log.d("UserWithPosts", "Posts list is null or empty for user: " + user.getUserName());
                        }
                    }
                    // Mettez à jour l'adaptateur après avoir ajouté toutes les publications
                    postAdapter.notifyDataSetChanged();
                } else {
                    Log.d("UserWithPosts", "UserWithPosts list is null");
                }
            }

        }.execute();
    }
} */
public class HomeFragment extends Fragment implements PostAdapter.OnDetailsButtonClickListener {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList; // Utilisez cette liste pour afficher les publications

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Initialiser le RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postList = new ArrayList<>(); // Utilisez la liste des publications
        postAdapter = new PostAdapter(postList); // Utilisez la liste des publications
        postAdapter.setOnDetailsButtonClickListener(this);

        recyclerView.setAdapter(postAdapter);

        // Charger les données depuis Room
        loadPostsFromRoom();

        return view;
    }

    private void loadPostsFromRoom() {
        new AsyncTask<Void, Void, List<Post>>() {
            @Override
            protected List<Post> doInBackground(Void... voids) {
                // Récupérez la liste des publications directement depuis la base de données Room
                return AppDatabase.getAppDatabase(requireContext()).postDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Post> posts) {
                super.onPostExecute(posts);

                if (posts != null) {
                    postList.addAll(posts);
                    // Mettez à jour l'adaptateur après avoir ajouté toutes les publications
                    postAdapter.notifyDataSetChanged();
                } else {
                    Log.d("HomeFragment", "Posts list is null");
                }
            }

        }.execute();
    }
    @Override
    public void onDetailsButtonClick(Post post) {
        // Récupérez le post par son ID depuis la base de données Room
        LiveData<Post> postLiveData = AppDatabase.getAppDatabase(requireContext()).postDao().findPostById(post.getId());

        // Observer pour les changements dans les détails du post
        postLiveData.observe(getViewLifecycleOwner(), postDetails -> {
            if (postDetails != null) {
                Log.d("HomeFragment", "Post details received: " + postDetails.getTitle());

                // Créez un objet PostWithComments pour passer le post et ses commentaires au fragment de détails
                PostWithCommentaires postWithComments = new PostWithCommentaires(postDetails, new ArrayList<>());

                // Remplacez le fragment actuel par le fragment de détails avec le post et ses commentaires
                PostDetails detailsFragment = new PostDetails();

                Bundle args = new Bundle();
                args.putSerializable("post_with_comments", postWithComments);

                detailsFragment.setArguments(args);

               /* getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_home, detailsFragment)
                        .addToBackStack(null)
                        .commit();
*/

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_home, detailsFragment)
                        .addToBackStack(null)
                        .commit();

            } else {
                Log.d("HomeFragment", "Post details are null");
                // Gérez le cas où les détails du post ne sont pas disponibles
            }
        });
    }


}

