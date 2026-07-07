/**
 * services/commentService.js
 */
const commentService = {
  addComment(contractId, content) {
    return api.post('/comments', content, {
      params: { contractId },
      headers: { 'Content-Type': 'text/plain' }
    }).then(res => res.data);
  },
  getComments(contractId) {
    return api.get(`/comments/${contractId}`).then(res => res.data);
  }
};
