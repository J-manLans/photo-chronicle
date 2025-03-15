# Creating a New Branch

- **`git checkout main`**: Switches to the `main` branch.

- **`git fetch origin`**: Fetches the latest changes from the remote repository, but it doesn't merge them into our local branch.

- **`git reset --hard origin/main`**: This command **resets  the local `main` branch** to exactly match the remote `main` (overwriting any local changes in the process). This ensures that the local `main` is in sync with the remote and reflects the latest state of the code.

If you feel frisky you can also just try a `git pull` `:)`.

---

# Manually Merging a Pull Request

## First Workflow:

```bash
# Switch to the main branch
git checkout main
# Pull the latest changes from the remote main (origin/main)
git pull
# Pull the feature branch from the remote and merge it into the local main
git pull origin feature-branch
# Push the updated main branch to the remote repository
git push
```

## Second Workflow:

```bash
# Switch to the main branch
git checkout main
# Fetch all the latest changes from the remote (but don't merge them yet)
git fetch origin
# Reset your local main branch to exactly match the remote origin/main (overwrites local changes)
git reset --hard origin/main
# Merge the feature branch from the remote into your local main
git merge origin/feature-branch
# Push the updated main branch to the remote repository
git push
```

## Key Differences:

- **In the first workflow**, we pull both the latest updates from `main` and the feature branch, merging it in the process. This is sufficient if we just want to get the latest changes and start working on merging the feature.

- **In the second workflow**, we're making sure that our local `main` is **fully synchronized** with the remote `main` by using `git reset --hard`. This ensures the local `main` branch is clean and in exact sync with the remote before merging in the feature branch. It's useful if we're concerned that our local `main` has diverged or has local changes we don't want. A sort of belt and suspenders tactic.